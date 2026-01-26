package com.katachi.blog.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

/**
 * フォームで入力されたパスワード(password, passwordConfirmation)が同じであることをチェックする
 */
public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, Object> {

	private String defaultMessage;

	@Override
	public void initialize(ConfirmPassword constraintAnnotation) {
		this.defaultMessage = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object form, ConstraintValidatorContext context) {
		
		// フォームに入力された値を取得する
		BeanWrapper beanWrapper = new BeanWrapperImpl(form);
		String password = (String)beanWrapper.getPropertyValue("password");
		String passwordConfirmation = (String)beanWrapper.getPropertyValue("passwordConfirmation");
		
		// 一致すればOK
		if (password != null && password.equals(passwordConfirmation)) {
			return true;
		}

		// 一致しなければ、デフォルトで作成される制約違反オブジェクトの生成を無効にし、
		// 新たに"password"プロパティの制約違反オブジェクトを生成する
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(this.defaultMessage)
			.addPropertyNode("password")
			.addConstraintViolation();
		return false;
	}
}
