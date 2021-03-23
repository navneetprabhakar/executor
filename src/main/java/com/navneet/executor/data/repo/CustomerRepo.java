package com.navneet.executor.data.repo;

import com.navneet.executor.data.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer findByCustomerId(String customerId);

    List<Customer> findAllByName(String name);
}
