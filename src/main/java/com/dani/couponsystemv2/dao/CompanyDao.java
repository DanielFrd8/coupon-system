package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public interface CompanyDao {

    boolean existsByEmailAndPassword(String email, String password);

    Optional<Company> findByEmailAndPassword(String email, String password);

    Company addCompany(Company company) throws IllegalStateException;

    Company updateCompany(Function<Company, Company> mapper, Long id)
            throws DoesntExistException, IllegalStateException;

    Company deleteCompany(Long id) throws DoesntExistException;

    List<Company> findAll();

    Optional<Company> findById(Long id);

    boolean existsByName(String name);

    boolean existsByEmail(String email);
}
