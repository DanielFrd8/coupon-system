package com.dani.couponsystemv2.jwt;

import com.dani.couponsystemv2.Services.JwtService;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.facades.CustomerFacade;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
class CustomerTests {

    @Autowired
    private JwtService service;

    @Test
    void contextLoads() {
    }

    @Test
    void encode(){
        Company company= new Company("rainbow","rain@bow.com","461a49ea5c854e284ee2a513f87eadcf");
        company.setId(1L);

        System.out.println(service.encodeCompany(company));
    }

    @Test
    void decode(){
        String jwt ="eyJhbGciOiJIUzI1NiJ9.eyJzY29wZSI6IkNPTVBBTlkiLCJuYW1lIjoicmFpbkBib3cuY29tIiwiaWQiOjF9.kHR56tNTLyDAUrnIFV5LtL-7XPhfR1JNLNhTSlGVLy8";

        System.out.println(service.decodeJwt(jwt));
    }
}
