package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.CollateralRequest;
import com.app.bankingloanservice.dto.DocumentResponse;
import com.app.bankingloanservice.dto.DocumentUploadRequest;
import com.app.bankingloanservice.entity.Collateral;

public interface CollateralService {

    Collateral createCollateral(CollateralRequest collateralRequest);

    DocumentResponse uploadCollateralDocument (Long collateralId, DocumentUploadRequest documentUploadRequest);
}
