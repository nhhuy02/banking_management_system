package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.DocumentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DocumentUploadRequest {

    @Schema(description = "Loan application ID")
    @NotNull(message = "Loan application ID is required")
    private Long loanApplicationId;

    @Schema(description = "The document type")
    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    @Schema(description = "The document file to be uploaded")
    @NotNull(message = "Document file is required")
    private MultipartFile documentFile;

    @Schema(description = "Optional description of the document")
    private String description;

}
