package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DocumentResponseDto {

    private Long documentId;
    private Long loanApplicationId;
    private String fileName;
    private DocumentType documentType;
    private String fileType;
    private Long fileSize;
    private String description;

}
