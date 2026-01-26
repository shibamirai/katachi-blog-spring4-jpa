package com.katachi.blog.form;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.katachi.blog.validation.ValidCategory;

import lombok.Data;

@Data
public class PostForm {

	@NotBlank
	private String title;

	private String thumbnail;

	private MultipartFile thumbnailFile;

	@NotBlank
	private String body;

	@ValidCategory
	private int categoryId;

}
