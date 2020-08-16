package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Coupon;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.stream.Stream;

@SpringBootTest
class CouponTests {

    @Autowired
    private CouponDao couponDao;

    @Test
    void contextLoads() {
    }

    @Test
    void add() {
        Coupon coupon = new Coupon(
                "basket-ball game",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                5,
                19.9,
                "image2",
                "free basket-ball game"
        );
        try {
            System.out.println(couponDao.addCoupon(coupon,3L,CategoryType.SPORT));
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void findById(){
        System.out.println(couponDao.findById(1L));
    }

    @Test
    void findAll(){
        couponDao.findAll().forEach(System.out::println);
    }

    @Test
    void update(){
        Function<Coupon,Coupon> mapper = coupon -> {
            coupon.setDescription("basketball with michael jordan");
            return coupon;
        };

        try {
            Coupon updated = couponDao.updateCoupon(mapper,5L);
            System.out.println(updated);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void delete(){
        try {
            System.out.println(couponDao.deleteCoupon(2L,4L));
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void addCustomerCoupon(){
        try {
            Coupon coupon = couponDao.addCouponPurchase(3L,1L);
            System.out.println(coupon);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void removeCustomerCoupon(){
        try {
            Coupon coupon = couponDao.removeCouponPurchase(3L,1L);
            System.out.println(coupon);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
