package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends CrudRepository<Coupon, Long> {

    List<Coupon> findByCompanyIdAndCategory(Long companyId, Category category);

    boolean existsByTitle(String title);

    @Override
    @Modifying
    @Transactional
    @Query("delete from Coupon c where c.id = :id")
    void deleteById(@Param("id") Long id);
}
