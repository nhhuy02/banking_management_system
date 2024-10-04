package com.ctv_it.klb.util;

import com.ctv_it.klb.enumeration.ReportFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

  private final TemplateEngine templateEngine;
  private static final String PATTERN = "P@TTERN";
  private static final String BASE_DIR = "./file/";

  public byte[] export(ReportFormat reportFormat, String fileName, String templateName,
      Map<String, Object> data) {
    String targetFile = BASE_DIR + fileName;

    try {
      ensureDirectoryExists(targetFile);
      return processFileExport(reportFormat, templateName, data, targetFile);
    } catch (IOException e) {
      log.error("Error occurred while exporting the report: {}", e.getMessage(), e);
      throw new InternalError();
    }
  }

  private void ensureDirectoryExists(String targetFile) throws IOException {
    File file = new File(targetFile);
    File parentDir = file.getParentFile();
    if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
      log.error("Failed to create directories: {}", parentDir.getAbsolutePath());
      throw new IOException("Failed to create directories");
    }
    log.info("Directories are already existed: {}", parentDir);
  }

  private byte[] processFileExport(ReportFormat reportFormat, String templateName,
      Map<String, Object> data, String targetFile) throws IOException {
    try (OutputStream outStream = new FileOutputStream(new File(targetFile))) {
      log.info("File exporting is processing");

      if (ReportFormat.EXCEL.equals(reportFormat)) {
        writeFileExcel(outStream, templateName, data);
      } else if (ReportFormat.PDF.equals(reportFormat)) {
        writeFilePDF(outStream, templateName, data);
      } else {
        log.error("Unsupported to export for type {}", reportFormat.name());
        throw new InternalError();
      }

      log.info("File is exported successfully");
      return readFile(targetFile);
    }
  }

  private void writeFilePDF(OutputStream outStream, String pathTemplateName,
      Map<String, Object> data) {
    log.debug("Start creation of PDF");

    try (InputStream input = getTemplateInputStream(pathTemplateName)) {
      org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
      data.forEach(context::setVariable); // Fill context with data

      // Process the HTML template
      String processedHtml = templateEngine.process(pathTemplateName, context);

      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(
          Arrays.toString(processedHtml.getBytes(StandardCharsets.UTF_8)));
      renderer.layout();

      // Create the PDF
      renderer.createPDF(outStream, true);

      log.info("PDF created successfully for template {}", pathTemplateName);
    } catch (IOException e) {
      log.error("IOException while processing template {}: {}", pathTemplateName, e.toString());
      throw new InternalError();
    } catch (Exception e) {
      log.error("General error while generating the PDF document: {}", e.toString());
      throw new InternalError();
    }
  }

  private void writeFileExcel(OutputStream outStream, String pathTemplateName,
      Map<String, Object> data) {
    log.debug("Start creation of Excel");

    try (InputStream input = getTemplateInputStream(pathTemplateName)) {
      Context context = new Context();
      data.forEach(context::putVar); // Fill context with data

      JxlsHelper.getInstance().processTemplate(input, outStream, context);
      log.info("Excel created successfully for template {}", pathTemplateName);
    } catch (IOException e) {
      log.error("IOException while processing template {}: {}", pathTemplateName, e.getMessage(),
          e);
      throw new InternalError();
    } catch (Exception e) {
      log.error("General error while generating the Excel document: {}", e.getMessage(), e);
      throw new InternalError();
    }
  }

  private InputStream getTemplateInputStream(String pathTemplateName) {
    InputStream input = getClass().getResourceAsStream(pathTemplateName);
    if (input == null) {
      log.error("Template file not found for {}", pathTemplateName);
      throw new InternalError();
    }
    return input;
  }

  private byte[] readFile(String filePath) throws IOException {
    File file = new File(filePath);
    try (FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
      byte[] data = new byte[1024];
      int bytesRead;
      while ((bytesRead = fis.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, bytesRead);
      }
      return buffer.toByteArray();
    }
  }

  public byte[] addFileNameToData(byte[] data, String fileName) {
    byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
    byte[] patternBytes = PATTERN.getBytes(StandardCharsets.UTF_8);

    byte[] combined = new byte[data.length + patternBytes.length + fileNameBytes.length];

    System.arraycopy(data, 0, combined, 0, data.length);
    System.arraycopy(patternBytes, 0, combined, data.length, patternBytes.length);
    System.arraycopy(fileNameBytes, 0, combined, data.length + patternBytes.length,
        fileNameBytes.length);

    return combined;
  }

  public Map<String, Object> splitDataAndFileName(byte[] combined) {
    byte[] patternBytes = PATTERN.getBytes(StandardCharsets.UTF_8);
    int patternIndex = indexOf(combined, patternBytes);

    if (patternIndex == -1) {
      log.error("PATTERN {} is not found", PATTERN);
      throw new InternalError();
    }

    byte[] data = Arrays.copyOfRange(combined, 0, patternIndex);
    byte[] fileNameBytes = Arrays.copyOfRange(combined, patternIndex + patternBytes.length,
        combined.length);
    String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);

    return Map.of("data", data, "fileName", fileName);
  }

  private int indexOf(byte[] array, byte[] target) {
    for (int i = 0; i <= array.length - target.length; i++) {
      boolean found = true;
      for (int j = 0; j < target.length; j++) {
        if (array[i + j] != target[j]) {
          found = false;
          break;
        }
      }
      if (found) {
        return i;
      }
    }
    return -1;
  }
}
