package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Customer;
import com.dani.couponsystemv2.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.codec.digest.DigestUtils.*;

@AllArgsConstructor
@Primary
@Component
public class CustomerDbdao implements CustomerDao {

    private final CustomerRepository repository;

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        return repository.existsByEmailAndPassword(email, md5Hex(password));
    }

    @Override
    public Customer addCustomer(Customer customer) throws IllegalStateException {
        customer.setPassword(md5Hex(customer.getPassword()));
        return repository.save(customer);
    }

    @Override
    public Customer updateCustomer(Function<Customer, Customer> mapper, Long id) throws DoesntExistException, IllegalStateException {
        return findById(id)
                .map(byId -> mapper.andThen(repository::save).apply(byId))
                .orElseThrow(() ->
                        new DoesntExistException(
                                "Customer by the id " + id + " does not exist in order to update"
                        ));
    }

    @Override
    public Customer deleteCustomer(Long id) throws DoesntExistException {
        return findById(id)
                .map(customer -> {
                    repository.delete(customer);
                    return customer;
                }).orElseThrow(() -> new DoesntExistException(
                        "Customer by the id " + id +
                                " does not exists in order to delete"
                ));
    }

    @Override
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Customer> findByEmailAndPassword(String email, String password) {
        return repository.findByEmailAndPassword(email, md5Hex(password));
    }

    @Override
    public List<Customer> findByCouponsId(Long couponId) {
        return repository.findByCouponsId(couponId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
