package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.Document;
import com.app.bankingloanservice.constant.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Find documents by loan application ID
    List<Document> findByLoanApplication_LoanApplicationId(Long loanApplicationId);

    // Find documents by document type
    List<Document> findByDocumentType(DocumentType documentType);

}
