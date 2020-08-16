package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import com.dani.couponsystemv2.repository.CategoryRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Primary
@Component
public class CategoryDbdao implements CategoryDao {

    private final CategoryRepository repository;

    public CategoryDbdao(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Category> findByType(CategoryType type) {
        return repository.findByType(type);
    }

    @Override
    public Category getOrCreate(CategoryType type) {
        return findByType(type).orElseGet(() -> repository.save(new Category(type)));
    }
}
