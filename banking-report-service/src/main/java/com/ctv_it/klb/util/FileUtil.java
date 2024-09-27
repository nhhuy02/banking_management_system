package com.ctv_it.klb.util;

import com.ctv_it.klb.common.Default;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileUtil {

  public byte[] writeFile(String fileName, String templateName, Map<String, Object> data) {
    try {
      File file = new File(fileName);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        boolean dirsCreated = parentDir.mkdirs();  // Create directories if they don't exist
        if (dirsCreated) {
          log.info("Created missing directories: {}", parentDir.getAbsolutePath());
        } else {
          log.error("Failed to create directories: {}", parentDir.getAbsolutePath());
          throw new IOException("Failed to create directories");
        }
      }

      // Create and export the document using FileExportUtil
      try (OutputStream outStream = new FileOutputStream(file)) {
        this.writeFile(outStream, templateName, data);
        log.info("File is exported successfully");

        return this.readFile(fileName);  // Return the file as a byte array
      }
    } catch (IOException e) {
      log.error("Error occurred while exporting the report", e);
      throw new InternalError("Error while exporting report");
    }
  }

  public void writeFile(OutputStream outStream, String pathTemplateName, Map<String, Object> data) {
    log.debug("Start creation of document");
    log.info("pathTemplateName: {}", pathTemplateName);

    try (InputStream input = this.getClass().getResourceAsStream(pathTemplateName)) {
      if (input == null) {
        log.error("Template file not found: {}", pathTemplateName);
        throw new FileNotFoundException("Template file not found: " + pathTemplateName);
      }

      Context context = new Context();
      // Fill context with data
      data.forEach(context::putVar);

      // Process the template and write to the output stream
      JxlsHelper.getInstance().processTemplate(input, outStream, context);
      log.info("Template processed successfully");

    } catch (FileNotFoundException e) {
      log.error("FileNotFoundException: {}", e.getMessage(), e);
      throw new InternalError("Error generating the document: Template not found", e);
    } catch (IOException e) {
      log.error("IOException while processing template", e);
      throw new InternalError("Error generating the document: I/O exception", e);
    } catch (Exception e) {
      log.error("General error while generating the document", e);
      throw new InternalError("Error generating the document", e);
    }
  }

  public byte[] readFile(String filePath) throws IOException {
    File file = new File(filePath);
    try (FileInputStream fis = new FileInputStream(file)) {
      return fis.readAllBytes();
    }
  }

  // Method to add filename to data
  public byte[] addFileNameToData(byte[] data, String fileName) {
    // Convert the filename to bytes using UTF-8 encoding
    byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
    // Convert the pattern to bytes
    byte[] patternBytes = Default.File.PATTERN.getBytes(StandardCharsets.UTF_8);

    // Create a new array to hold the combined data
    byte[] combined = new byte[data.length + patternBytes.length + fileNameBytes.length];

    // Copy the original data, pattern, and filename bytes into the new array
    System.arraycopy(data, 0, combined, 0, data.length);
    System.arraycopy(patternBytes, 0, combined, data.length, patternBytes.length);
    System.arraycopy(fileNameBytes, 0, combined, data.length + patternBytes.length,
        fileNameBytes.length);

    return combined;
  }

  // Method to split combined byte array back into data and filename
  public Map<String, Object> splitDataAndFileName(byte[] combined) {
    // Convert the pattern to bytes
    byte[] patternBytes = Default.File.PATTERN.getBytes(StandardCharsets.UTF_8);

    // Find the index of the pattern in the combined array
    int patternIndex = indexOf(combined, patternBytes);
    if (patternIndex == -1) {
      log.error("PATTERN {} is not found", Default.File.PATTERN);
      return null; // or throw an exception
    }

    // Extract data and filename based on the pattern index
    byte[] data = Arrays.copyOfRange(combined, 0, patternIndex);
    byte[] fileNameBytes = Arrays.copyOfRange(combined, patternIndex + patternBytes.length,
        combined.length);

    // Create a result object and return it
    String fileName = new String(fileNameBytes, StandardCharsets.UTF_8);
    return Map.of("data", data, "fileName", fileName);

  }

  // Helper method to find the index of a byte array within another byte array
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
        return i; // Return the index where the pattern is found
      }
    }
    return -1; // Pattern not found
  }

  public String generateReportingFileName(ReportRequestDTO requestDTO) {
    return requestDTO.getReportType() + "_" + requestDTO.getReportFormat() + "_"
        + LocalDateTime.now();
  }
}
