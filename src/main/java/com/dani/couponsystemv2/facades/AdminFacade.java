package com.dani.couponsystemv2.facades;

import com.dani.couponsystemv2.dao.CompanyDao;
import com.dani.couponsystemv2.dao.CouponDao;
import com.dani.couponsystemv2.dao.CustomerDao;
import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.exceptions.LoggedOutException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.model.Customer;
import com.dani.couponsystemv2.validation.CompanyValidation;
import com.dani.couponsystemv2.validation.CustomerValidation;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Component
public class AdminFacade extends ClientFacade {

    @Value("${admin-email}")
    private String adminEmail;

    @Value("${admin-password}")
    private String adminPassword;

    private final CompanyValidation companyValidation;
    private final CustomerValidation customerValidation;

    @Getter
    private boolean isLoggedIn = false;

    public AdminFacade(CompanyDao companyDao, CouponDao couponDao, CustomerDao customerDao, CompanyValidation companyValidation, CustomerValidation customerValidation) {
        super(companyDao, couponDao, customerDao);
        this.companyValidation = companyValidation;
        this.customerValidation = customerValidation;
    }

    @Override
    public boolean login(String email, String password) {
        return isLoggedIn = (email.equals(adminEmail) && password.equals(adminPassword));
    }

    public Company addCompany(Company company) throws IllegalStateException {
//        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.addCompany(
                companyValidation.validateAttributes
                        .andThen(companyValidation.validateExistence)
                        .apply(company)
        );
    }

    public Company updateCompany(String email, String password, Long companyId) throws LoggedOutException, DoesntExistException, IllegalStateException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.updateCompany(company -> {
            company.setEmail(email);
            companyDao.findById(companyId).map(comp -> comp.getPassword().equals(password)).ifPresent(
                    isMatch -> {
                        if (isMatch) {
                            company.setPassword(password);
                        } else {
                            company.setPassword(md5Hex(password));
                        }
                    }
            );
            return companyValidation.validateAttributes
                    .andThen(c -> {
                        try {
                            if (company.getId() == companyId &&
                                    company.getEmail().equals(getOneCompany(companyId).getEmail()))
                                return company;
                        } catch (LoggedOutException | DoesntExistException e) {
                            e.printStackTrace();
                        }
                        return companyValidation.validateEmailExistence.apply(c);
                    })
                    .apply(company);
        }, companyId);
    }

    public Company deleteCompany(Long companyId) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.findById(companyId).map(company -> {
            company.getCoupons().forEach(coupon ->
                    customerDao.findByCouponsId(coupon.getId())
                            .forEach(customer -> {
                                try {
                                    couponDao.removeCouponPurchase(
                                            customer.getId(),
                                            coupon.getId()
                                    );
                                } catch (DoesntExistException e) {
                                    e.printStackTrace();
                                }
                            }));
            try {
                companyDao.deleteCompany(companyId);
            } catch (DoesntExistException e) {
                e.printStackTrace();
            }
            return company;
        }).orElseThrow(() -> new DoesntExistException(
                "Company by the id " + companyId +
                        " does not exists in order to delete"
        ));
    }

    public List<Company> getAllCompanies() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        List<Company> companies = companyDao.findAll();
//        companies.forEach(company -> company.getCoupons().forEach(coupon -> coupon.setCompany(null)));
//        companies.forEach(company -> company.getCoupons().forEach(coupon -> coupon.getCustomers().forEach(customer -> customer.setCoupons(null))));
        return companies;
    }

    public Company getOneCompany(Long id) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        Company company = companyDao.findById(id).orElseThrow(() -> new DoesntExistException("The company with id " + id + " does not exist"));
//        company.getCoupons().forEach(coupon -> coupon.setCompany(null));
//        company.getCoupons().forEach(coupon -> coupon.getCustomers().forEach(customer -> customer.setCoupons(null)));
        return company;
    }

    public Customer addCustomer(Customer customer) throws IllegalStateException {
//        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.addCustomer(
                customerValidation
                        .validateAttributes.
                        andThen(customerValidation.validateExistence)
                        .apply(customer)
        );
    }

    public Customer updateCustomer(Customer customer) throws
            LoggedOutException, IllegalStateException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.updateCustomer(c -> {
            c.setEmail(customer.getEmail());
            customerDao.findById(customer.getId()).map(cust -> cust.getPassword().equals(customer.getPassword())).ifPresent(
                    isMatch -> {
                        if (isMatch) {
                            c.setPassword(customer.getPassword());
                        } else {
                            c.setPassword(md5Hex(customer.getPassword()));
                        }
                    }
            );
            return customerValidation
                    .validateAttributes
                    .andThen(cust -> {
                        try {
                            if (cust.getId() == customer.getId() &&
                                    cust.getEmail().equals(getOneCustomer(customer.getId()).getEmail()))
                                return cust;
                        } catch (LoggedOutException | DoesntExistException e) {
                            e.printStackTrace();
                        }
                        return customerValidation.validateExistence.apply(cust);
                    }).apply(c);
        }, customer.getId());
    }

    public Customer deleteCustomer(Long id) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.findById(id).map(customer -> {
            customer.getCoupons().forEach(coupon -> {
                try {
                    couponDao.removeCouponPurchase(id, coupon.getId());
                } catch (DoesntExistException e) {
                    e.printStackTrace();
                }
            });
            try {
                customerDao.deleteCustomer(id);
            } catch (DoesntExistException e) {
                e.printStackTrace();
            }
            return customer;
        }).orElseThrow(() -> new DoesntExistException(
                "Customer by the id " + id +
                        "does not exists in order to delete"
        ));
    }

    public List<Customer> getAllCustomers() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        List<Customer> customers = customerDao.findAll();
//        customers.forEach(customer -> customer.getCoupons().forEach(coupon -> coupon.getCompany().setCoupons(null)));
//        customers.forEach(customer -> customer.getCoupons().forEach(coupon -> coupon.setCustomers(null)));
        return customers;
    }

    public Customer getOneCustomer(Long id) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        Customer customer = customerDao.findById(id).orElseThrow(() ->
                new DoesntExistException(
                        "The customer with id " + id + " does not exist"
                ));
//        customer.getCoupons().forEach(coupon -> coupon.getCompany().setCoupons(null));
//        customer.getCoupons().forEach(coupon -> coupon.setCustomers(null));
        return customer;
    }

}