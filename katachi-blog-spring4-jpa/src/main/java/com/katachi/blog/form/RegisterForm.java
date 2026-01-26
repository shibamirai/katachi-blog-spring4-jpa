package com.katachi.blog.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterForm {

	@NotBlank
	private String name;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String passwordConfirmation;

}
