package com.katachi.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.katachi.blog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
