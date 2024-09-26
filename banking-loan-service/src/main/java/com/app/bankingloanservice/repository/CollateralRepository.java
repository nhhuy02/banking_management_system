package com.app.bankingloanservice.repository;

import com.app.bankingloanservice.entity.Collateral;
import com.app.bankingloanservice.constant.CollateralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollateralRepository extends JpaRepository<Collateral, Long> {

    // Find collaterals by loan application ID
    List<Collateral> findByLoanApplication_LoanApplicationId(Long loanApplicationId);

    // Find collaterals by loan ID
    List<Collateral> findByLoan_LoanId(Long loanId);

    // Find collaterals by collateral status
    List<Collateral> findByStatus(CollateralStatus status);

    // Custom query to find collaterals that have been reclaimed
    @Query("SELECT c FROM Collateral c WHERE c.reclaimDate IS NOT NULL")
    List<Collateral> findReclaimedCollaterals();

    // Custom query to find collaterals by collateral type and value greater than a specific amount
    @Query("SELECT c FROM Collateral c WHERE c.collateralType = :collateralType AND c.collateralValue > :minValue")
    List<Collateral> findByTypeAndMinValue(@Param("collateralType") String collateralType, @Param("minValue") Long minValue);

    // Custom query to find collaterals by release date
    @Query("SELECT c FROM Collateral c WHERE c.releaseDate = :releaseDate")
    List<Collateral> findByReleaseDate(@Param("releaseDate") String releaseDate);

    // Custom query to find collaterals modified after a specific date
    @Query("SELECT c FROM Collateral c WHERE c.lastModifiedDate > :lastModifiedDate")
    List<Collateral> findModifiedAfter(@Param("lastModifiedDate") String lastModifiedDate);
}
