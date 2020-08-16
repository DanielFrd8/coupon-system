package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.exceptions.DoesntExistException;
import com.dani.couponsystemv2.model.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

@SpringBootTest
class CompanyTests {

    @Autowired
    private CompanyDao companyDao;

    @Test
    void contextLoads() {
    }

    @Test
    void add() {
        Stream.of(
                new Company(
                        "rainbow",
                        "rain@bow.com",
                        "l2323"
                ), new Company(
                        "yes",
                        "yes@no.com",
                        "223354"
                ), new Company(
                        "bug",
                        "bug@bug.com",
                        "545454"
                ), new Company(
                        "sports",
                        "spo@rts.com",
                        "443219"
                )).map(companyDao::addCompany)
                .forEach(System.out::println);
    }

    @Test
    void existByEmailAndPassword() {
        System.out.println(
                companyDao.existsByEmailAndPassword("yes@no.com", "223354")
        );
    }

    @Test
    void existByEmail() {
        System.out.println(
                companyDao.existByEmail("yes@no.com")
        );
    }

    @Test
    void existByName() {
        System.out.println(
                companyDao.existByName("bug")
        );
    }

    @Test
    void findById() {
        System.out.println(
                companyDao.findById(4L)
        );
    }

    @Test
    void findAll() {
        companyDao.findAll().forEach(System.out::println);
    }

    @Test
    void delete() {
        try {
            Company deleted = companyDao.deleteCompany(3L);
            System.out.println(deleted);
        } catch (DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void update() {

        try {

            Function<Company, Company> mapper = company -> {
                company.setName("yes no");
                return company;
            };

            Company updated = companyDao.updateCompany(mapper, 2L);
            System.out.println(updated);

        } catch (IllegalStateException | DoesntExistException e) {
            System.out.println(e.getMessage());
        }
    }

}
