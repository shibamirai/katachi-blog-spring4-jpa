package com.katachi.blog.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CommentForm {

	@NotBlank
	private String body;

}
