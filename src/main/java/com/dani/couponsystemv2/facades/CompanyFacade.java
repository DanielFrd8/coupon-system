package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.validation.CouponValidation;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompanyFacade extends ClientFacade {
    private Long companyId;

    @Getter
    private boolean isLoggedIn = false;

    private final CouponValidation couponValidation;

    public CompanyFacade(CompanyDao companyDao, CouponDao couponDao, CustomerDao customerDao, CouponValidation couponValidation) {
        super(companyDao, couponDao, customerDao);
        this.couponValidation = couponValidation;
    }

    @Override
    public boolean login(String email, String password) {
        if (!companyDao.existsByEmailAndPassword(email, password)) return isLoggedIn;
        Company company = companyDao.findByEmailAndPassword(email, password).get();
        companyId = company.getId();
        isLoggedIn = true;
        return isLoggedIn;
    }

    public Coupon addCoupon(Coupon coupon, CategoryType category) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return couponDao.addCoupon(
                couponValidation.
                        validateAttributes.
                        andThen(couponValidation.validateTitleExistence)
                        .apply(coupon)
                , companyId
                , category
        );
    }

    public Coupon updateCoupon(Coupon coupon) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return couponDao.updateCoupon(c -> {
            c.setTitle(coupon.getTitle());
            c.setStartDate(coupon.getStartDate());
            c.setEndDate(coupon.getEndDate());
            c.setPrice(coupon.getPrice());
            c.setImage(coupon.getImage());
            c.setDescription(coupon.getDescription());
            c.setImage(coupon.getImage());
            return couponValidation.validateAttributes
                    .andThen(cpn -> {
                        if (cpn.getId() == coupon.getId() &&
                                cpn.getTitle().equals(couponDao.findById(coupon.getId()).get().getTitle()))
                            return cpn;
                        return couponValidation.validateTitleExistence.apply(c);
                    })
                    .apply(c);
        }, coupon.getId());
    }

    public Coupon deleteCoupon(Long couponId) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return couponDao.findById(couponId).map(coupon -> {
            coupon.customerList()
                    .forEach(customer -> {
                        try {
                            couponDao.removeCouponPurchase(
                                    customer.getId(),
                                    couponId
                            );
                        } catch (DoesntExistException e) {
                            e.printStackTrace();
                        }
                    });
            try {
                couponDao.deleteCoupon(couponId, companyId);
            } catch (DoesntExistException e) {
                e.printStackTrace();
            }
            return coupon;
        }).orElseThrow(() -> new DoesntExistException(
                "Coupon by the id " + couponId +
                        " does not exists in order to delete"
        ));
    }

    public List<Coupon> getCompanyCoupons() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return getCompanyDetails().getCoupons();
    }

    public List<Coupon> getCompanyCoupons(CategoryType category) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return couponDao.findByCompanyIdAndCategory(companyId,category);
    }

    public List<Coupon> getCompanyCoupons(double maxPrice) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        List<Coupon> coupons = new ArrayList<>();
        getCompanyCoupons().forEach(coupon -> {
            if (coupon.getPrice() <=  maxPrice){
                coupons.add(coupon);
            }
        });
        return coupons;
    }

    public Company getCompanyDetails() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.findById(companyId).get();
    }
}
