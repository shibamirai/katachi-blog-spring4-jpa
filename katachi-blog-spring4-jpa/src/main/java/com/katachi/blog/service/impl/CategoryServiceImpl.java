package com.katachi.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.katachi.blog.model.Category;
import com.katachi.blog.repository.CategoryRepository;
import com.katachi.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category getCategory(Integer id) {
		return categoryRepository.findById(id).orElseThrow();
	}

	@Override
	public boolean exists(Integer id) {
		return categoryRepository.existsById(id);
	}

}
