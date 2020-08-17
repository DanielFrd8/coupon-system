package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

@SpringBootTest
class CompanyTests {

    @Autowired
    private CompanyFacade companyFacade;

    @BeforeEach
    void init() {
        boolean isLoggedIn = companyFacade.login("burgers@bar.com", "123456");
    }

    @Test
    void contextLoads() {
    }

    @Test
    void printLoggedIn() {
        System.out.println(companyFacade.isLoggedIn());
    }

    @Test
    void login() {

        boolean isIt = companyFacade.login("burgers@bar.com", "123456");
        System.out.println(isIt);
        System.out.println(companyFacade.isLoggedIn());
    }

    @Test
    void addCoupon() {
        try {
            Coupon added = companyFacade.addCoupon(new Coupon(
                    "book gift card",
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    7,
                    15.6,
                    "image2",
                    "second book for free"
            ), CategoryType.GIFT);
            System.out.println(added);
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            Stream.of(e.getSuppressed())
                    .map(Throwable::getMessage)
                    .forEach(System.out::println);
        }
    }

    @Test
    void updateCoupon() {
        try {
            Coupon coupon =new Coupon(
                "book gift card",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                10,
                24,
                "image2",
                "second book for free"
            );
            coupon.setId(2L);
            Coupon updated = companyFacade.updateCoupon(coupon);
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
    void deleteCoupon() {
        try {
            Coupon deleted = companyFacade.deleteCoupon(3L);
            System.out.println(deleted);
        } catch (LoggedOutException | DoesntExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCompanyCoupons() {
        try {
            companyFacade.getCompanyCoupons().forEach(System.out::println);
        } catch (LoggedOutException  e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCompanyCouponsByCategory() {
        try {
            companyFacade.getCompanyCoupons(CategoryType.GIFT).forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCompanyCouponsByMaxPrice() {
        try {
            companyFacade.getCompanyCoupons(15).forEach(System.out::println);
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getCompanyDetails() {
        try {
            System.out.println(companyFacade.getCompanyDetails());
        } catch (LoggedOutException e) {
            e.printStackTrace();
        }
    }
}
