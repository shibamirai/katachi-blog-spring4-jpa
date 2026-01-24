package com.katachi.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.katachi.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

	/** slug で記事を検索。投稿者およびカテゴリー情報も含める */
	@Query("select p from Post p join fetch p.user join fetch p.category where p.slug = :slug")
	Optional<Post> findBySlug(String slug);

}
