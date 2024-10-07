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
      log.error("Export(format={}, filename={}, templateName={}, data={}) failed: \n{}",
          reportFormat, fileName, templateName, data, e.toString());

      throw new InternalError();
    }
  }

  private void ensureDirectoryExists(String targetFile) throws IOException {
    File file = new File(targetFile);
    File parentDir = file.getParentFile();

    if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
      log.error("Failed to create directories for targetFile: {}", targetFile);
      throw new InternalError();
    }
    log.info("Directories checked or created: {} for file: {}", parentDir, targetFile);
  }

  private void processFileExport(ReportFormat reportFormat, String templateName,
      Map<String, Object> data, String targetFile) throws IOException {
    File target = new File(targetFile);

    try (OutputStream outStream = new FileOutputStream(target)) {
      log.info("File export in progress for format: {}", reportFormat);
      writeFileBasedOnFormat(reportFormat, outStream, templateName, data);
      log.info("File export completed successfully");
    } catch (Exception e) {
      log.error("File export failed: {}", e.getMessage());
      cleanupIncompleteFile(targetFile);
      throw new InternalError();
    }
  }

  private void cleanupIncompleteFile(String targetFile) {
    File target = new File(targetFile);
    if (target.exists() && !target.delete()) {
      log.error("Failed to delete incomplete file: {}", targetFile);
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
        log.error("Unsupported export format: {}", reportFormat.name());
        throw new InternalError();
    }
  }

  private void writeFilePDF(OutputStream outStream, String templateName, Map<String, Object> data) {
    log.info("Generating PDF for template: {}", templateName);
    try (InputStream input = getTemplateInputStream(templateName);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

      String processedHtml = processHtmlTemplate(templateName, data);
      ITextRenderer renderer = createPdfRenderer(processedHtml);

      renderer.createPDF(byteArrayOutputStream, false);
      renderer.finishPDF();

      outStream.write(byteArrayOutputStream.toByteArray());
      log.info("PDF generation completed for template: {}", templateName);
    } catch (Exception e) {
      log.error("PDF generation failed for template: {}\n{}", templateName, e.toString());
      throw new InternalError();
    }
  }

  private String processHtmlTemplate(String templateName, Map<String, Object> data) {
    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    data.forEach((key, value) -> context.setVariable(key, value instanceof String
        ? new String(((String) value).getBytes(), StandardCharsets.UTF_8) : value));

    return templateEngine.process(templateName, context);
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

  private void writeFileExcel(OutputStream outStream, String templateName,
      Map<String, Object> data) {

    log.info("Generating Excel for template: {}", templateName);
    try (InputStream input = getTemplateInputStream(templateName)) {
      Context context = new Context();
      data.forEach(context::putVar);

      JxlsHelper.getInstance().processTemplate(input, outStream, context);
      log.info("Excel generation completed for template: {}", templateName);
    } catch (Exception e) {
      log.error("Excel generation failed for template: {}, error: {}", templateName, e.toString());

      throw new InternalError();
    }
  }

  private InputStream getTemplateInputStream(String templateName) {
    InputStream input = getClass().getResourceAsStream(templateName);
    if (input == null) {
      log.error("Template file not found: {}", templateName);
      throw new InternalError();
    }
    return input;
  }

  public Resource readFile(String fileName) {
    if (fileName == null || fileName.isEmpty()) {
      log.warn("File name is null or empty for download.");
      throw new InternalError();
    }

    try {
      Resource resource = new UrlResource(Paths.get(fileName).toUri());
      if (!resource.exists() || !resource.isReadable()) {
        log.warn("File not found or unreadable: {}", fileName);
        throw new InternalError();
      }
      log.info("File read successfully: {}", fileName);
      return resource;
    } catch (MalformedURLException e) {
      log.error("Malformed URL for file: {}, error: {}", fileName, e.toString());
      throw new InternalError();
    }
  }
}
