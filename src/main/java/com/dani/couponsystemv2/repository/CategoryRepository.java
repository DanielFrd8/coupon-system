package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByType(CategoryType type);
}
