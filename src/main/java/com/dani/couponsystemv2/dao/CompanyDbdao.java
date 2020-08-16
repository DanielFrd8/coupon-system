package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import com.dani.couponsystemv2.repository.CompanyRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.codec.digest.DigestUtils.*;

@Primary
@Component
public class CompanyDbdao implements CompanyDao {

    private final CompanyRepository repository;

    public CompanyDbdao(CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        return repository.existsByEmailAndPassword(email, md5Hex(password));
    }

    @Override
    public Company addCompany(Company company) throws IllegalStateException {
        company.setPassword(md5Hex(company.getPassword()));
        return repository.save(company);
    }

    @Override
    public Company updateCompany(Function<Company, Company> mapper, Long id) throws DoesntExistException, IllegalStateException {
        return findById(id)
                .map(byId -> mapper.andThen(repository::save).apply(byId)).orElseThrow(() -> new DoesntExistException(
                        "Company by the id " + id + " does not exist in order to update"
                ));
    }

    @Modifying
    @Transactional
    @Override
    public Company deleteCompany(Long id) throws DoesntExistException {
        return findById(id)
                .map(byId -> {
                    repository.deleteById(id);
                    return byId;
                })
                .orElseThrow(() -> new DoesntExistException(
                        "Company by the id " + id + " does not exist in order to delete"
                ));
    }

    @Override
    public List<Company> findAll() {
        List<Company> companies = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(companies::add);
        return companies;
    }

    @Override
    public Optional<Company> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public boolean existByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
