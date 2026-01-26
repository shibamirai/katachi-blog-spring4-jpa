package com.katachi.blog.service;

import java.util.List;

import com.katachi.blog.model.Comment;

public interface CommentService {

	List<Comment> getByPostId(Integer postId);

}
