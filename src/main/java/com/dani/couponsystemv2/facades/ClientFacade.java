package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public abstract class ClientFacade {
    protected final CompanyDao companyDao;
    protected final CouponDao couponDao;
    protected final CustomerDao customerDao;

    public abstract boolean login(String email, String password);
}
