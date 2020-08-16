package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, Long> {

    Optional<Coupon> findByIdAndCustomersId(Long couponId,Long customerId);
    boolean existsByIdAndCustomersId(Long couponId,Long customerId);
    Optional<Coupon> findByIdAndCompanyId(Long couponId,Long companyId);

    @Override
    @Modifying
    @Transactional
    @Query("delete from Coupon c where c.id = :id")
    void deleteById(@Param("id") Long id);
}
