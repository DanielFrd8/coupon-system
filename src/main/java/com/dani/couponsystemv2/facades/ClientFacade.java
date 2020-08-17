package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import org.springframework.stereotype.Service;

@Service
public abstract class ClientFacade {
    protected final CompanyDao companyDao;
    protected final CouponDao couponDao;
    protected final CustomerDao customerDao;

    protected final String LOGGED_OUT_MESSAGE = "In order to continue, you must log in";

    public ClientFacade(CompanyDao companyDao, CouponDao couponDao, CustomerDao customerDao) {
        this.companyDao = companyDao;
        this.couponDao = couponDao;
        this.customerDao = customerDao;
    }

    public abstract boolean login(String email, String password);
}
