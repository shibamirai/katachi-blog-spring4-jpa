package com.katachi.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.katachi.blog.model.Post;

@Service
public interface PostService {

	List<Post> getPosts();

}
