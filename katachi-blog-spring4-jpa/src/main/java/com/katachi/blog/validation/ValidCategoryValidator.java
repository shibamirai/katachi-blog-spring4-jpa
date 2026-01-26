package com.katachi.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.katachi.blog.service.CategoryService;

public class ValidCategoryValidator implements ConstraintValidator<ValidCategory, Integer> {

	@Autowired
	private CategoryService categoryService;

	@Override
	public boolean isValid(Integer categoryId, ConstraintValidatorContext context) {
		return categoryService.exists(categoryId);
	}
}
