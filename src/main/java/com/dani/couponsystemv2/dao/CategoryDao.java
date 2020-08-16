package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CategoryDao {
    Optional<Category> findByType(CategoryType type);

    Category getOrCreate(CategoryType type);
}
