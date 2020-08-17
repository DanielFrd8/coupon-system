package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerFacade extends ClientFacade {

    private Long customerId;

    @Getter
    private boolean isLoggedIn = false;

    public CustomerFacade(CompanyDao companyDao, CouponDao couponDao, CustomerDao customerDao) {
        super(companyDao, couponDao, customerDao);
    }

    @Override
    public boolean login(String email, String password) {
        if (!customerDao.existsByEmailAndPassword(email, password)) return false;
        Customer customer = customerDao.findByEmailAndPassword(email, password).get();
        customerId = customer.getId();
        isLoggedIn = true;
        return isLoggedIn;
    }

    public Coupon purchaseCoupon(Coupon coupon) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        couponDao.findById(coupon.getId()).map(cpn -> {
            int amount = cpn.getAmount();
            if (amount > 0) {
                cpn.setAmount(amount - 1);
            }
            return cpn;
        }).orElseThrow(() -> new DoesntExistException(
                "Coupon by the id " + coupon.getId() + " does not exists"
        ));
        return couponDao.addCouponPurchase(customerId, coupon.getId());
    }

    public List<Coupon> getCustomerCoupons() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return getCustomerDetails().getCoupons();
    }

    public List<Coupon> getCustomerCoupons(CategoryType category) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return couponDao.findByCustomersIdAndCategory(customerId, category);
    }

    public List<Coupon> getCustomerCoupons(double maxPrice) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        List<Coupon> coupons = new ArrayList<>();
        getCustomerCoupons().forEach(coupon -> {
            if (coupon.getPrice() <= maxPrice) {
                coupons.add(coupon);
            }
        });
        return coupons;
    }

    public Customer getCustomerDetails() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.findById(customerId).get();
    }
}
