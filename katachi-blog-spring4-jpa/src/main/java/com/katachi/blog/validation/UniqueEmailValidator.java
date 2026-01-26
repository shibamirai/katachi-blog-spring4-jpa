package com.katachi.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.katachi.blog.service.UserService;

/**
 * 同じメールアドレスを持つユーザーがまだ登録されていないことを確認する
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserService userService;

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		
		if (email == null) {
			return true;
		}
		return !userService.existsByEmail(email);
	}
}
