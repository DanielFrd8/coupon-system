package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndPassword(String email,String password);

    List<Customer> findByCouponsId(Long couponId);
}
