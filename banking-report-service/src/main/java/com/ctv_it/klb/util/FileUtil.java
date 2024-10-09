package com.ctv_it.klb.util;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
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

  // Export method, supports EXCEL and PDF formats
  public Resource export(ReportFormat reportFormat, String fileName, String templateName,
      Map<String, Object> data) {
    String targetFile = BASE_DIR + fileName;

    try {
      ensureDirectoryExists(targetFile);
      processFileExport(reportFormat, templateName, data, targetFile);
      return readFile(targetFile);
    } catch (IOException e) {
      log.error("Export failed: format={}, filename={}, templateName={}, data={}\n{}", reportFormat,
          fileName, templateName, data, e.toString());
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
    log.info("Directories checked or created: {}", targetFile);
  }

  private void processFileExport(ReportFormat reportFormat, String templateName,
      Map<String, Object> data, String targetFile) throws IOException {
    File target = new File(targetFile);
    try (OutputStream outStream = new FileOutputStream(target)) {
      log.info("File export in progress for format: {}", reportFormat);
      writeFileBasedOnFormat(reportFormat, outStream, templateName, data);
      log.info("File export completed successfully.");
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

      addPageNumbers(byteArrayOutputStream, outStream);

      log.info("PDF generation completed for template: {}", templateName);
    } catch (Exception e) {
      log.error("PDF generation failed for template: {}\n{}", templateName, e.toString());
      throw new InternalError();
    }
  }

  private void addPageNumbers(ByteArrayOutputStream byteArrayOutputStream, OutputStream outStream)
      throws Exception {

    PdfReader pdfReader = new PdfReader(byteArrayOutputStream.toByteArray());
    PdfStamper pdfStamper = new PdfStamper(pdfReader, outStream);
    int totalPages = pdfReader.getNumberOfPages();
    float pageWidth = pdfReader.getPageSize(1).getWidth(); // Get the width of the page

    // Create the font outside the loop for efficiency
    BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI,
        BaseFont.EMBEDDED);
    float fontSize = 10; // Adjust the font size if necessary

    for (int i = 1; i <= totalPages; i++) {
      PdfContentByte contentByte = pdfStamper.getOverContent(i);
      contentByte.beginText();
      contentByte.setFontAndSize(baseFont, fontSize);

      // Construct the page number text
      String pageNumberText = "Trang(Page) " + i + "/" + totalPages;


      // Calculate the text width and center it
      float textWidth = baseFont.getWidth(pageNumberText) * fontSize / 1000; // Get width in points
      float x = (pageWidth - textWidth) / 2; // Center the text horizontally
      float y = 30; // Set y-position above the bottom edge (adjust as needed)

      // Show text aligned to the calculated position
      contentByte.showTextAligned(PdfContentByte.ALIGN_CENTER, pageNumberText, x + (textWidth / 2),
          y, 0);
      contentByte.endText();
    }

    pdfStamper.close();
    pdfReader.close();
  }

  private String processHtmlTemplate(String templateName, Map<String, Object> data) {
    org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
    data.forEach((key, value) -> context.setVariable(key,
        value instanceof String ? new String(((String) value).getBytes(), StandardCharsets.UTF_8)
            : value));

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
      log.error("Excel generation failed for template: {}\n{}", templateName, e.toString());
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
