package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import com.dani.couponsystemv2.repository.CompanyRepository;
import com.dani.couponsystemv2.repository.CouponRepository;
import com.dani.couponsystemv2.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Primary
@Component
public class CouponDbdao implements CouponDao {
    private final CouponRepository couponRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CategoryDao categoryDao;


    @Override
    public Coupon addCoupon(Coupon coupon, Long companyId, CategoryType type) throws DoesntExistException, IllegalStateException {
        return companyRepository.findById(companyId)
                .map(company -> {
                    coupon.setCompany(company);
                    coupon.setCategory(categoryDao.getOrCreate(type));
                    return couponRepository.save(coupon);
                }).orElseThrow(() -> new DoesntExistException(
                        "Company by the id " + companyId + " does not exist"
                ));
    }

    @Override
    public Coupon updateCoupon(Function<Coupon, Coupon> mapper, Long id) throws DoesntExistException {
        return findById(id)
                .map(byId -> mapper.andThen(couponRepository::save).apply(byId))
                .orElseThrow(() -> new DoesntExistException(
                        "Coupon by the id " + id + " does not exist in order to update"
                ));
    }

    @Modifying
    @Transactional
    @Override
    public Coupon deleteCoupon(Long couponId, Long companyId) throws DoesntExistException {
        return findById(couponId).map(coupon -> {
            couponRepository.deleteById(couponId);
            return coupon;
        }).orElseThrow(() -> new DoesntExistException(
                        "Coupon by the id " + couponId +
                                "doesnt exists in order to delete"
                )
        );
    }

    @Override
    public List<Coupon> findAll() {
        List<Coupon> coupons = new ArrayList<>();
        couponRepository.findAll().forEach(coupons::add);
        return coupons;
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return couponRepository.findById(id);
    }

    @Override
    public boolean existsByTitle(String title) {
        return couponRepository.existsByTitle(title);
    }

    @Override
    public List<Coupon> findByCompanyIdAndCategory(Long companyId, CategoryType categoryType) {
        return couponRepository.findByCompanyIdAndCategory(companyId,categoryDao.getOrCreate(categoryType));
    }


    @Override
    public Coupon addCouponPurchase(Long customerId, Long couponId) throws DoesntExistException {
        return findById(couponId).map(coupon ->
                customerRepository.findById(customerId).map(customer ->  {
                    List<Coupon> customerCoupons = customer.getCoupons();
                    if (!customerCoupons.contains(coupon)) {
                        customerCoupons.add(coupon);
                        customer.setCoupons(customerCoupons);
                        customerRepository.save(customer);
                        return coupon;
                    }
                    return coupon;
                }).orElse(null))
                .orElseThrow(() -> new DoesntExistException(
                        "Coupon or Customer by: " +
                                new HashMap<String, Long>(){{
                                    put("Customer ID", customerId);
                                    put("Coupon ID", couponId);
                                }}
                                +
                                "doesnt exists in order to purchase"
                ));
    }

    @Modifying
    @Transactional
    @Override
    public Coupon removeCouponPurchase(Long customerId, Long couponId) throws DoesntExistException {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setCoupons(
                    customer.getCoupons()
                            .stream()
                            .filter(c -> !c.getId().equals(couponId))
                            .collect(Collectors.toList())
            );
            customerRepository.save(customer);
            return findById(couponId).orElse(null);
        }).orElseThrow(() -> new DoesntExistException(
                "Coupon or Customer by: " +
                        new HashMap<String, Long>(){{
                            put("Customer ID", customerId);
                            put("Coupon ID", couponId);
                        }}
                        +
                        "doesnt exists in order to remove purchase"
        ));
    }
}
