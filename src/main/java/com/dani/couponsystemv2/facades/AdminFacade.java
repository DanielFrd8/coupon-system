package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Coupon;
import com.dani.couponsystemv2.model.Customer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminFacade extends ClientFacade {

    @Value("${admin-email}")
    private String adminEmail;

    @Value("${admin-password}")
    private String adminPassword;

    @Getter
    private boolean isLoggedIn = false;

    public AdminFacade(CompanyDao companyDao, CouponDao couponDao, CustomerDao customerDao) {
        super(companyDao, couponDao,customerDao);
    }

    @Override
    public boolean login(String email, String password) {
        return isLoggedIn = (email.equals(adminEmail) && password.equals(adminPassword));
    }

    public Company addCompany(Company company){
        return companyDao.addCompany(company);
    }

    public Company updateCompany(Company company) throws DoesntExistException {
        return companyDao.updateCompany(beforeUpdate -> {
            beforeUpdate.setName(company.getName());
            beforeUpdate.setEmail(company.getEmail());
            beforeUpdate.setPassword(company.getPassword());
            beforeUpdate.setCoupons(company.getCoupons());
            return beforeUpdate;
        },company.getId());
    }

    public Company deleteCompany(Long companyId) throws DoesntExistException {
      return companyDao.deleteCompany(companyId);
    }

    public List<Company> getAllCompany(){
        return companyDao.findAll();
    }

    public Company getOneCompany(Long id) throws DoesntExistException {
        return companyDao.findById(id).orElseThrow(() ->
                new DoesntExistException("Company by the id: "+ id + "does not exist")
        );
    }

    public Customer addCustomer(Customer customer){
        return customerDao.addCustomer(customer);
    }

    public Customer updateCustomer(Customer customer) throws DoesntExistException {
        return customerDao.updateCustomer(beforeUpdate -> {
            beforeUpdate.setFirstName(customer.getFirstName());
            beforeUpdate.setLastName(customer.getLastName());
            beforeUpdate.setEmail(customer.getEmail());
            beforeUpdate.setPassword(customer.getPassword());
            beforeUpdate.setCoupons(customer.getCoupons());
            return beforeUpdate;
        },customer.getId());
    }

    public Customer deleteCustomer(Long id) throws DoesntExistException {
        Customer customer = getOneCustomer(id);
        List<Coupon> coupons = getOneCustomer(id).getCoupons();
        for (Coupon coupon:coupons) {
            couponDao.updateCoupon(coupon1 -> {
                coupon1.setAmount(coupon.getAmount()+1);
                return coupon1;
            },coupon.getId());
            couponDao.removeCouponPurchase(id,coupon.getId());
        }
        return customer;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.findAll();
    }

    public Customer getOneCustomer(Long id) throws DoesntExistException {
        return customerDao.findById(id).orElseThrow(() ->
                new DoesntExistException("Customer by the id: "+ id+ " does not exist")
        );
    }
}
