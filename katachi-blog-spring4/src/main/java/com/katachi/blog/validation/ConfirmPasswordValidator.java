package com.katachi.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {

	@Override
	public boolean isValid(Object form, ConstraintValidatorContext context) {
		
		BeanWrapper beanWrapper = new BeanWrapperImpl(form);
		String password = (String)beanWrapper.getPropertyValue("password");
		String passwordConfirmation = (String)beanWrapper.getPropertyValue("passwordConfirmation");
		
		if (password != null && password.equals(passwordConfirmation)) {
			return true;
		}

		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("パスワードが一致しません")
			.addPropertyNode("password")
			.addConstraintViolation();
		return false;
	}
}
