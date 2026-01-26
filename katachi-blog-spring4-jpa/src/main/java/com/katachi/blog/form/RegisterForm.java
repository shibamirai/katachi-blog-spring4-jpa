package com.katachi.blog.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.katachi.blog.validation.ConfirmPassword;
import com.katachi.blog.validation.UniqueEmail;

import lombok.Data;

@Data
@ConfirmPassword
public class RegisterForm {

	@NotBlank
	private String name;

	@NotBlank
	@Email
	@UniqueEmail
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String passwordConfirmation;

}
