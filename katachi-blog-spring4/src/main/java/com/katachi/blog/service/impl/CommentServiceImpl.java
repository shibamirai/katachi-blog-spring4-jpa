package com.katachi.blog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.model.Comment;
import com.katachi.blog.repository.CommentRepository;
import com.katachi.blog.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<Comment> getByPostId(Integer postId) {
		return commentRepository.findByPostId(postId);
	}

	@Override
	public Comment post(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	@PostAuthorize("returnObject.userId == authentication.principal.id")
	public Comment getMyComment(Integer commentId) {
		return commentRepository.findById(commentId).orElseThrow(ResourceNotFoundException::new);
	}
}
