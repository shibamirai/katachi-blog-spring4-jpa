package com.katachi.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.katachi.blog.model.Post;
import com.katachi.blog.repository.PostRepository;
import com.katachi.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Override
	public List<Post> getPosts() {
		return postRepository.findAll();
	}

}
