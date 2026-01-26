package com.katachi.blog.service;

import java.util.List;

import com.katachi.blog.model.Comment;

public interface CommentService {

	List<Comment> getByPostId(Integer postId);

	Comment getMyComment(Integer commentId);

	Comment post(Comment comment);

	Comment update(Comment comment);

}
