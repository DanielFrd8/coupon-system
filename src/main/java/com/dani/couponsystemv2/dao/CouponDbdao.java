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
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
        return couponRepository.findByIdAndCompanyId(couponId, companyId)
                .map(byId -> {
                    couponRepository.deleteById(byId.getId());
                    return byId;
                }).orElseThrow(() -> new DoesntExistException(
                        "Coupon by the id " + couponId + " does not exist in order to delete"
                ));
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
    public Coupon addCouponPurchase(Long customerId, Long couponId) throws DoesntExistException {
        Customer customer = isCustomerExists(customerId);
        Coupon coupon = isCouponExists(couponId);
        customer.getCoupons().add(coupon);
        customerRepository.save(customer);
        coupon.getCustomers().add(customer);
        return coupon;
    }

    @Modifying
    @Transactional
    @Override
    public Coupon removeCouponPurchase(Long customerId, Long couponId) throws DoesntExistException {
        Customer customer = isCustomerExists(customerId);
        Coupon coupon = isCouponExists(couponId);
        customer.getCoupons().remove(coupon);
        customerRepository.save(customer);
        coupon.getCustomers().remove(customer);
        return coupon;
    }

    private Customer isCustomerExists(Long id) throws DoesntExistException {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new DoesntExistException("The customer with id: " + id + "does not exists")
                );
    }

    private Coupon isCouponExists(Long id) throws DoesntExistException{
        return couponRepository.findById(id).orElseThrow(() ->
                new DoesntExistException(
                        "Coupon with the id: " +id + " does not exist")
        );
    }
}
