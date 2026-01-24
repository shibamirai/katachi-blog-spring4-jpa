package com.katachi.blog.service;

import java.util.Optional;

public class PostCriteria {

	private final Optional<String> titleOrBody;

	public PostCriteria(Optional<String> titleOrBody) {
		this.titleOrBody = titleOrBody;
	}
	
	public Optional<String> getTitleOrBody() {
		return this.titleOrBody;
	}

}
