package com.katachi.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.katachi.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

	@Query("select p from Post p join fetch p.category order by p.postedAt desc")
	List<Post> findAll();

}
