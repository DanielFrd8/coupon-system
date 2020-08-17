package com.dani.couponsystemv2.validation;

import com.dani.couponsystemv2.dao.CustomerDao;
import com.dani.couponsystemv2.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CustomerValidation extends EntityValidation {

    @Autowired
    private CustomerDao customerDao;

    public Function<Customer, Customer> validateExistence = customer -> Validator.of(customer)
            .validate(
                    Customer::getEmail,
                    email -> !customerDao.existsByEmail(email),
                    existsByError(Customer.class.getSimpleName(),customer.getEmail(), "email")
            )
            .get();

    public Function<Customer, Customer> validateAttributes = customer -> Validator.of(customer)
            .validate(
                    Customer::getFirstName,
                    isValidRange(2, 30),
                    lengthError(FIRST_NAME, 2)
            )
            .validate(
                    Customer::getLastName,
                    isValidRange(2, 30),
                    lengthError(LAST_NAME, 2)
            )
            .validate(
                    Customer::getEmail,
                    isValidRange(5, 30),
                    lengthError(EMAIL, 5)
            )
            .validate(
                    Customer::getPassword,
                    isValidRange(5, 255),
                    lengthError(PASSWORD, 5)
            )
            .get();
}
