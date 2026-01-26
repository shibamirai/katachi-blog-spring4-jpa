package com.katachi.blog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.katachi.blog.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	@Query("select c from Comment c join fetch c.user where c.postId = :postId")
	List<Comment> findByPostId(Integer postId);

	@Query("select c from Comment c join fetch c.user where c.id = :id")
	Optional<Comment> findById(Integer id);

}
