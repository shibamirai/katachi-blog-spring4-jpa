package com.katachi.blog.service.impl;

import java.util.Optional;

import jakarta.persistence.criteria.JoinType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.katachi.blog.model.Post;
import com.katachi.blog.repository.PostRepository;
import com.katachi.blog.service.PostCriteria;
import com.katachi.blog.service.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	/**
	 * categoryをjoin fetchするSpecification作成
	 */
	private Specification<Post> join() {
		return (root, query, builder) -> {

			/* ページングを行う findAll では select count が実行されたときに例外が発生するため
			 * クエリの戻り値の型が Long のとき(=select count)は join fetch しないようにする
			 * 参考：https://hepokon365.hatenablog.com/entry/2021/12/31/160502
			 */
			if (query.getResultType() != Long.class) {
				root.fetch("category", JoinType.INNER);
			}
			return builder.conjunction();
		};
	}

	/** タイトル検索用Specification */
	private Specification<Post> titleContains(Optional<String> search) {
		return search.isEmpty() ? Specification.unrestricted() : (root, _, builder) -> {
			return builder.like(root.get("title"), "%" + search.get() + "%");
		};
	}

	/** 記事本文検索用Specification */
	private Specification<Post> bodyContains(Optional<String> search) {
		return search.isEmpty() ? Specification.unrestricted() : (root, _, builder) -> {
			return builder.like(root.get("body"), "%" + search.get() + "%");
		};
	}

	@Override
	public Page<Post> getPosts(PostCriteria postCriteria, Pageable pageable) {
		Optional<String> search = postCriteria.getTitleOrBody();
		return postRepository.findAll(
			join()
			.and(Specification.anyOf(titleContains(search), bodyContains(search))),
			pageable
		);
	}

}
