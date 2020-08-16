package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class AdminTests {

    @Autowired
    private AdminFacade adminFacade;

    @BeforeEach
    void init() {
        boolean isLoggedIn = adminFacade.login("admin@admin.com", "admin");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void printLoggedIn() {
        System.out.println(adminFacade.isLoggedIn());
    }

    @Test
    void addCompany(){
        Stream.of(new Company(
                "burger"
                ,"bur@ger.com",
                "123456")
        ).map(adminFacade::addCompany)
        .forEach(System.out::println);
    }

    @Test
    void updateCompany(){
        Company company = new Company(
                "burger"
                ,"bur@ger.com",
                "123456");
        company.setId(5L);
        try {
            adminFacade.updateCompany(company);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
