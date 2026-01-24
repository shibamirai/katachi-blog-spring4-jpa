package com.katachi.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.katachi.blog.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
