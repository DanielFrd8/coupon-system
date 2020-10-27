package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class AdminTests {

    @Autowired
    private AdminFacade adminFacade;

    @Value("${admin-email}")
    public String username;

    @Value("${admin-password}")
    public String password;

    @BeforeEach
    void init() {
        boolean isLoggedIn = adminFacade.login(username, password);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void printLoggedIn() {
        System.out.println(adminFacade.isLoggedIn());
    }

    @Test
    void login() {

        boolean isIt = adminFacade.login("admin@admin.com", "admin");
        System.out.println(isIt);
        System.out.println(adminFacade.isLoggedIn());
    }

    @Test
    void addCompany() {
        try {
            Company added = adminFacade.addCompany(new Company(
                    "Leumi",
                    "leu@mi.com",
                    "123123"
            ));
            System.out.println(added);
//        } catch (LoggedOutException e) {
//            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void updateCompany() {

        try {
            Company updated = adminFacade.updateCompany(
                    "burgers@bar.com", "123456", 9L
            );
            System.out.println(updated);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        } catch (DoesntExistException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void deleteCompany() {
        try {
            Company deleted = adminFacade.deleteCompany(3L);
            System.out.println(deleted);
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findAllCompanies() {
        try {
            adminFacade.getAllCompanies().forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void findOneCompany() {
        try {
            System.out.println(adminFacade.getOneCompany(8L));
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addCustomer() {
        try {
            System.out.println(
                    adminFacade.addCustomer(new Customer(
                            "sonya",
                            "wins",
                            "sonya@wins.com",
                            "123478"
                    )));
//        } catch (LoggedOutException e) {
//            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void updateCustomer(){
        try {
            Customer updated = adminFacade.updateCustomer(
                    new Customer(
                            5L,
                            "sonya",
                            "wins",
                            "sonya@wins.com",
                            "12334"
                    )
            );
            System.out.println(updated);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        } catch (DoesntExistException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void deleteCustomer(){
        try {
            Customer deleted = adminFacade.deleteCustomer(10L);
            System.out.println(deleted);
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllCustomers(){
        try {
            adminFacade.getAllCustomers().forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getOneCustomer(){
        try {
            System.out.println(adminFacade.getOneCustomer(4L));
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        }
    }
}
