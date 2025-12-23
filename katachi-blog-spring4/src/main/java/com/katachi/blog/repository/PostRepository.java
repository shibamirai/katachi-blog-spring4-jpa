package com.katachi.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.katachi.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

}
