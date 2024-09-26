package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.DocumentUploadDto;
import com.app.bankingloanservice.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentService {

    Document createDocument(DocumentUploadDto documentUploadDto);
}
