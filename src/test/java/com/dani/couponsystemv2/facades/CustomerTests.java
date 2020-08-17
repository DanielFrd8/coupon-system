package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest
class CustomerTests {

    @Autowired
    private CustomerFacade customerFacade;

    @BeforeEach
    void init() {
        boolean isLoggedIn = customerFacade.login("sonya@wins.com", "12334");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void printLoggedIn() {
        System.out.println(customerFacade.isLoggedIn());
    }

    @Test
    void login() {

        boolean isIt = customerFacade.login("sonya@wins.com", "12334");
        System.out.println(isIt);
        System.out.println(customerFacade.isLoggedIn());
    }

    @Test
    void addCouponPurchase() {
        try {
            Coupon coupon = new Coupon();
            coupon.setId(4L);
            Coupon purchased = customerFacade.purchaseCoupon(coupon);
            System.out.println(purchased);
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void getCustomerCoupons() {
        try {
            customerFacade.getCustomerCoupons().forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCustomerCouponsByCategory() {
        try {
            customerFacade.getCustomerCoupons(CategoryType.GIFT).forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCustomerCouponsByMaxPrice() {
        try {
            customerFacade.getCustomerCoupons(15).forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCompanyDetails() {
        try {
            System.out.println(customerFacade.getCustomerDetails());
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }
}
