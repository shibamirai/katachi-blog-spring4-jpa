package com.katachi.blog.form;

import jakarta.validation.constraints.NotBlank;

import com.katachi.blog.model.Comment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentForm {

	@NotBlank
	private String body;

	public CommentForm(String body) {
		this.body = body;
	}

	public Comment toComment() {
		Comment comment = new Comment();
		comment.setBody(body);
		return comment;
	}
}
