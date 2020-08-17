package com.dani.couponsystemv2.validation;

import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CouponValidation extends EntityValidation {

    @Autowired
    private CouponDao couponDao;

    public Function<Coupon, Coupon> validateTitleExistence = coupon -> Validator.of(coupon)
            .validate(
                    Coupon::getTitle,
                    name -> !couponDao.existsByTitle(name),
                    existsByError(Company.class.getSimpleName(), coupon.getTitle(), TITLE)
            )
            .get();

    public Function<Coupon, Coupon> validateAttributes = coupon -> Validator.of(coupon)
            .validate(
                    Coupon::getTitle,
                    isValidRange(2, 30),
                    lengthError(TITLE, 2)
            ).get();
}
