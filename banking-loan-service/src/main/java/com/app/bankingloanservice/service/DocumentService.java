package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.DocumentUploadRequest;
import com.app.bankingloanservice.entity.Document;

public interface DocumentService {

    Document createDocument(DocumentUploadRequest documentUploadRequest);
}
