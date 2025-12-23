package com.katachi.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.katachi.blog.model.Post;

public interface PostService {

	Page<Post> getPosts(Pageable pageable);
}
