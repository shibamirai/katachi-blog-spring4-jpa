package com.katachi.blog.service;

import java.util.List;

import com.katachi.blog.model.Category;

public interface CategoryService {

	List<Category> getCategories();

	Category getCategory(Integer id);

}
