package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public interface CustomerDao {
    boolean existsByEmailAndPassword(String email, String password);

    Customer addCustomer(Customer customer) throws IllegalStateException;

    Customer updateCustomer(Function<Customer, Customer> mapper, Long id)
            throws DoesntExistException, IllegalStateException;

    Customer deleteCustomer(Long id) throws DoesntExistException;

    List<Customer> findAll();

    Optional<Customer> findById(Long id);

    List<Customer> findByCouponsId(Long couponId);

    boolean existsByEmail(String email);
}
