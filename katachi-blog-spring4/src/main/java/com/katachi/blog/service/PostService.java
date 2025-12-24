package com.katachi.blog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.katachi.blog.model.Post;

public interface PostService {

	Page<Post> getPosts(
		Optional<String> search,
		Optional<Integer> categoryId,
		Pageable pageable
	);
	
	Post getPostBySlug(String slug);
}
