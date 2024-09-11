package com.ctv_it.customer_service.repository;

import com.ctv_it.customer_service.model.CustomersStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomersStatusHistoryRepository extends JpaRepository<CustomersStatusHistory, Long> {

    @Query(value = "SELECT * FROM customers_status_history WHERE customer_id = :customerId ORDER BY changed_at DESC LIMIT 1", nativeQuery = true)
    Optional<CustomersStatusHistory> findLatestByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT c FROM CustomersStatusHistory c WHERE c.customer.id = :customerId")
    List<CustomersStatusHistory> findAllByCustomerId(@Param("customerId") Long customerId);

}

