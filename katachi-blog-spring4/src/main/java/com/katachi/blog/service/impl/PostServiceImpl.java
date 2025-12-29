package com.katachi.blog.service.impl;

import java.util.Optional;

import jakarta.persistence.criteria.JoinType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.model.Category;
import com.katachi.blog.model.Post;
import com.katachi.blog.model.User;
import com.katachi.blog.repository.PostRepository;
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

	/** カテゴリ選択用Specification */
	private Specification<Post> categoryOf(Optional<Integer> categoryId) {
		return categoryId.isEmpty() ? Specification.unrestricted() : (root, _, builder) -> {
			return builder.equal(root.<Category>get("category").get("id"), categoryId.get());
		};
	}

	/** 投稿者名フィルタリング用Specification */
	private Specification<Post> postedBy(Optional<Integer> userId) {
		return userId.isEmpty() ? Specification.unrestricted() : (root, _, builder) -> {
			return builder.equal(root.<User>get("user").get("id"), userId.get());
		};
	}

	@Override
	public Page<Post> getPosts(
		Optional<String>  search,
		Optional<Integer> categoryId,
		Optional<Integer> userId,
		Pageable pageable
	) {
		return postRepository.findAll(
			join()
			.and(Specification.anyOf(titleContains(search), bodyContains(search)))
			.and(categoryOf(categoryId))
			.and(postedBy(userId)),
			pageable
		);
	}

	/** slugに該当する記事を返す。見つからなければResourceNotFoundExceptionを発生 */
	@Override
	public Post getPostBySlug(String slug) {
		return postRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException :: new);
	}

	/**
	 * ログインユーザーが投稿した記事を id で検索する。
	 * 見つからなければ ResourceNotFoundException を発生。
	 * 他人の記事であれば AuthenticationDeniedException を発生。
	 */
	@Override
	@PostAuthorize("returnObject.userId == principal.id")
	public Post getMyPostById(Integer id) {
		return postRepository.findById(id).orElseThrow(ResourceNotFoundException :: new);
	}

	@Override
	public Post post(Post post) {
		return postRepository.save(post);
	}
}
