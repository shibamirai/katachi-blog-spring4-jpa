package com.katachi.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.katachi.blog.model.Post;

@Service
public interface PostService {

	Page<Post> getPosts(PostCriteria postCriteria, Pageable pageable);

	Post getPostBySlug(String slug);

	Post getMyPostById(Integer id);

	/** 記事を投稿する */
	Post post(Post post);

	/** 記事を更新する */
	Post update(Post post);

	/** 記事を削除する */
	void delete(Post post);

}
