package com.ctv_it.customer_service.repository;

import com.ctv_it.customer_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountId(Long accountId);

    // Truy vấn để tìm khách hàng có email nhưng ID khác với customerId hiện tại
    Optional<Customer> findByEmailAndIdNot(String email, Long customerId);
}
