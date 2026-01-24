package com.katachi.blog.service.impl;

import java.util.List;

import jakarta.persistence.criteria.JoinType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.katachi.blog.model.Post;
import com.katachi.blog.repository.PostRepository;
import com.katachi.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	/**
	 * categoryをjoin fetchするSpecification作成
	 */
	private Specification<Post> join() {
		return (root, _, builder) -> {
			root.fetch("category", JoinType.INNER);
			return builder.conjunction();
		};
	}

	@Override
	public List<Post> getPosts() {
		return postRepository.findAll(join());
	}

}
