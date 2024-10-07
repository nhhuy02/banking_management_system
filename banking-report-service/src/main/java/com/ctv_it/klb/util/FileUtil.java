package com.ctv_it.klb.util;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUtil {

  private final TemplateEngine templateEngine;
  private static final String BASE_DIR = "./banking-report-service/file/";

  public Resource export(ReportFormat reportFormat, String fileName, String templateName,
      Map<String, Object> data) {

    String targetFile = BASE_DIR + fileName;

    try {
      ensureDirectoryExists(targetFile);
      processFileExport(reportFormat, templateName, data, targetFile);

      return readFile(targetFile);
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

  private void processFileExport(ReportFormat reportFormat, String templateName,
      Map<String, Object> data, String targetFile) throws IOException {

    File target = new File(targetFile);

    try (OutputStream outStream = new FileOutputStream(target)) {
      log.info("File exporting is processing");

      writeFileBasedOnFormat(reportFormat, outStream, templateName, data);
      log.info("File is exported successfully");

    } catch (IOException e) {
      log.error("Error occurred while exporting the file: {}", e.getMessage(), e);
      // Delete the partially created file to prevent incomplete file issues
      if (target.exists() && !target.delete()) {
        log.error("Failed to delete incomplete file: {}", targetFile);
      }
      throw new InternalError();
    } catch (Exception e) {
      log.error("General error while exporting the file: {}", e.getMessage(), e);
      if (target.exists() && !target.delete()) {
        log.error("Failed to delete incomplete file: {}", targetFile);
      }
      throw new InternalError();
    }
  }

  private void writeFileBasedOnFormat(ReportFormat reportFormat, OutputStream outStream,
      String templateName, Map<String, Object> data) throws IOException {
    switch (reportFormat) {
      case EXCEL:
        writeFileExcel(outStream, templateName, data);
        break;
      case PDF:
        writeFilePDF(outStream, templateName, data);
        break;
      default:
        log.error("Unsupported to export for type {}", reportFormat.name());
        throw new InternalError();
    }
  }

  private void writeFilePDF(OutputStream outStream, String pathTemplateName,
      Map<String, Object> data) {
    log.info("Start creation of PDF for template: {}", pathTemplateName);
    try (InputStream input = getTemplateInputStream(pathTemplateName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      String processedHtml = processHtmlTemplate(pathTemplateName, data);

      ITextRenderer renderer = createPdfRenderer(processedHtml);
      renderer.createPDF(byteArrayOutputStream, false);
      renderer.finishPDF();

      byte[] pdfBytes = byteArrayOutputStream.toByteArray();
      outStream.write(pdfBytes);

      log.info("PDF created successfully for template {}", pathTemplateName);
    } catch (IOException e) {
      log.error("IOException while processing template {}: {}", pathTemplateName, e.getMessage(),
          e);
      throw new InternalError();
    } catch (Exception e) {
      log.error("General error while generating the PDF document: {}", e.getMessage(), e);
      throw new InternalError();
    }
  }


  private String processHtmlTemplate(String pathTemplateName, Map<String, Object> data) {
    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    data.forEach((key, value) -> context.setVariable(key,
        value instanceof String ? new String(((String) value).getBytes(), StandardCharsets.UTF_8)
            : value));
    return templateEngine.process(pathTemplateName, context);
  }

  private ITextRenderer createPdfRenderer(String processedHtml)
      throws DocumentException, IOException {

    ITextRenderer renderer = new ITextRenderer();

    ITextFontResolver fontResolver = renderer.getFontResolver();
    addFonts(fontResolver);

    renderer.setDocumentFromString(processedHtml, null);
    renderer.layout();
    return renderer;
  }

  private void addFonts(ITextFontResolver fontResolver) throws DocumentException, IOException {
    fontResolver.addFont(new ClassPathResource("font/DejaVuSans.ttf").getPath(),
        BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
  }

  private void writeFileExcel(OutputStream outStream, String pathTemplateName,
      Map<String, Object> data) {

    log.debug("Start creation of Excel");
    try (InputStream input = getTemplateInputStream(pathTemplateName)) {
      Context context = new Context();
      data.forEach(context::putVar);
      JxlsHelper.getInstance().processTemplate(input, outStream, context);
      log.info("Excel file created successfully for template {}", pathTemplateName);
    } catch (Exception e) {
      log.error("Excel file created fail for template {}: {}", pathTemplateName, e.getMessage());
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

  public Resource readFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      log.warn("File name is null or empty for download request");
      throw new InternalError();
    }

    Resource resource;
    try {
      resource = new UrlResource(Paths.get(fileName).toUri());
      if (!resource.exists() || !resource.isReadable()) {
        log.warn("File not found or not readable: {}", fileName);
        throw new InternalError();
      }
    } catch (MalformedURLException e) {
      log.error("Malformed URL for file: {}", fileName, e);
      throw new InternalError();
    }

    log.info("Successfully read file: {}", fileName);
    return resource;
  }
}
