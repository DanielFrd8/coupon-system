package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.Function;
import java.util.stream.Stream;

@SpringBootTest
class CustomerTests {

    @Autowired
    private CustomerDao customerDao;

    @Test
    void contextLoads() {
    }

    @Test
    void add() {
        Stream.of(
                new Customer(
                        "bradley",
                        "peters",
                        "bradley@peters.com",
                        "l2323"
                ), new Customer(
                        "herb",
                        "cobbett",
                        "herb@cobbett.com",
                        "223354"
                ), new Customer(
                        "rosa",
                        "parker",
                        "rosa@parker.com",
                        "545454"
                ), new Customer(
                        "rex",
                        "lee",
                        "rex@lee.com",
                        "443219"
                )).map(customerDao::addCustomer)
                .forEach(System.out::println);
    }

    @Test
    void existByEmailAndPassword() {
        System.out.println(
                customerDao.existsByEmailAndPassword("yes@no.com", "223354")
        );
    }

    @Test
    void existByEmail() {
        System.out.println(
                customerDao.existsByEmail("yes@no.com")
        );
    }

    @Test
    void findById() {
        System.out.println(
                customerDao.findById(4L)
        );
    }

    @Test
    void findByCouponsId() {
        System.out.println(
                customerDao.findByCouponsId(4L)
        );
    }

    @Test
    void findAll() {
        customerDao.findAll().forEach(System.out::println);
    }

    @Test
    void delete() {
        try {
            Customer deleted = customerDao.deleteCustomer(3L);
            System.out.println(deleted);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void update() {

        try {

            Function<Customer, Customer> mapper = company -> {
                company.setEmail("rex@lee.com");
                return company;
            };

            Customer updated = customerDao.updateCustomer(mapper, 4L);
            System.out.println(updated);

        } catch (IllegalStateException | DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

}
