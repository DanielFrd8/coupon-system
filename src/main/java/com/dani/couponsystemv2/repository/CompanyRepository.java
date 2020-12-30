package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByEmailAndPassword(String email, String password);

    Optional<Company> findByEmailAndPassword(String email, String password);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    @Query("select c from Company c where c.email = :email")
    Optional<Company> findByEmail(@Param("email") String email);

    @Override
    @Modifying
    @Transactional
    @Query("delete from Company c where c.id = :id")
    void deleteById(@Param("id") Long id);
}
