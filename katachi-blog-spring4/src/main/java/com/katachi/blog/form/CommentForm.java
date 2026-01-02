package com.katachi.blog.form;

import jakarta.validation.constraints.NotBlank;

import com.katachi.blog.model.Comment;

import lombok.Data;

@Data
public class CommentForm {

	@NotBlank
	private String body;

	public Comment toComment() {
		Comment comment = new Comment();
		comment.setBody(body);
		return comment;
	}
}
