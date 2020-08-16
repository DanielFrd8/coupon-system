package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Coupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public interface CouponDao {

    Coupon addCoupon(Coupon coupon, Long companyId, CategoryType type)
            throws DoesntExistException, IllegalStateException;

    Coupon updateCoupon(Function<Coupon, Coupon> mapper, Long id)
            throws DoesntExistException;

    Coupon deleteCoupon(Long couponId, Long companyId)
            throws DoesntExistException;

    List<Coupon> findAll();

    Optional<Coupon> findById(Long id);

    Coupon addCouponPurchase(Long customerId, Long couponId)
            throws DoesntExistException;

    Coupon removeCouponPurchase(Long customerId, Long couponId)
            throws DoesntExistException;
}
