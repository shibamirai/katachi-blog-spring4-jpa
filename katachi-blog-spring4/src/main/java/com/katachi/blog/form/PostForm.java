package com.katachi.blog.form;

import jakarta.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.katachi.blog.model.Post;
import com.katachi.blog.validation.ValidCategory;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostForm {

	private int id;

	@NotBlank
	private String title;

	private String thumbnail;
	
	private boolean deleteThumbnail;

	private MultipartFile thumbnailFile;

	@NotBlank
	private String body;

	@ValidCategory
	private int categoryId;

	public PostForm(Post post) {
		id = post.getId();
		title = post.getTitle();
		thumbnail = post.getThumbnail();
		body = post.getBody();
		categoryId = post.getCategoryId();
	}
}
