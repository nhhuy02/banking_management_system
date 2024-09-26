package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.CollateralDto;
import com.app.bankingloanservice.dto.DocumentResponseDto;
import com.app.bankingloanservice.dto.DocumentUploadDto;
import com.app.bankingloanservice.entity.Collateral;
import org.springframework.web.multipart.MultipartFile;

public interface CollateralService {

    Collateral createCollateral(CollateralDto collateralDto);

    DocumentResponseDto uploadCollateralDocument (Long collateralId, DocumentUploadDto documentUploadDto);
}
