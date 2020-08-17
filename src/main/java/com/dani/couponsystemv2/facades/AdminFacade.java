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
import org.springframework.stereotype.Component;

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
        super(companyDao, couponDao,customerDao);
        this.companyValidation = companyValidation;
        this.customerValidation = customerValidation;
    }

    @Override
    public boolean login(String email, String password) {
        return isLoggedIn = (email.equals(adminEmail) && password.equals(adminPassword));
    }

    public Company addCompany(Company company) throws LoggedOutException, IllegalStateException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.addCompany(
                companyValidation.validateAttributes
                        .andThen(companyValidation.validateExistence)
                        .apply(company)
        );
    }

    public Company updateCompany(String email, String password, Long companyId) throws LoggedOutException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.updateCompany(company -> {
            company.setEmail(email);
            company.setPassword(md5Hex(password));
            return companyValidation.validateAttributes
                    .andThen(c -> {
                        try {
                            if (company.getId() == companyId &&
                                    company.getEmail().equals(getOneCompany(companyId).get().getEmail()))
                                return company;
                        } catch (LoggedOutException e) {
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

    public Iterable<Company> getAllCompanies() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.findAll();
    }

    public Optional<Company> getOneCompany(Long id) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return companyDao.findById(id);
    }

    public Customer addCustomer(Customer customer) throws LoggedOutException, IllegalStateException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.addCustomer(
                customerValidation
                        .validateAttributes.
                        andThen(customerValidation.validateExistence)
                        .apply(customer)
        );
    }

    public Customer updateCustomer(Customer customer) throws LoggedOutException, IllegalStateException, DoesntExistException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.updateCustomer(c -> {
            c.setFirstName(customer.getFirstName());
            c.setLastName(customer.getLastName());
            c.setEmail(customer.getEmail());
            c.setPassword(md5Hex(customer.getPassword()));
            return customerValidation
                    .validateAttributes
                    .andThen(cust -> {
                        try {
                            if (cust.getId() == customer.getId() &&
                                    cust.getEmail().equals(getOneCustomer(customer.getId()).get().getEmail()))
                                return cust;
                        } catch (LoggedOutException e) {
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

    public Iterable<Customer> getAllCustomers() throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.findAll();
    }

    public Optional<Customer> getOneCustomer(Long id) throws LoggedOutException {
        if (!isLoggedIn) throw new LoggedOutException(LOGGED_OUT_MESSAGE);
        return customerDao.findById(id);
    }

}