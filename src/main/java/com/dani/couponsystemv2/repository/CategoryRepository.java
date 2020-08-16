package com.dani.couponsystemv2.repository;

import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.model.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

    Optional<Category> findByType(CategoryType type);
}
