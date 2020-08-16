package com.dani.couponsystemv2.dao;

import com.dani.couponsystemv2.model.Category;
import com.dani.couponsystemv2.model.CategoryType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CategoryTests {

    @Autowired
    private CategoryDao categoryDao;

    @Test
    void contextLoads() {
    }

    @Test
    void finndByType() {
        Category food = new Category(CategoryType.FOOD);
        System.out.println(food);

        categoryDao.findByType(CategoryType.FOOD).ifPresent(System.out::println);
    }

    @Test
    void getOrCreate() {
        Category food = categoryDao.getOrCreate(CategoryType.FOOD);
        System.out.println(food);
    }

}
