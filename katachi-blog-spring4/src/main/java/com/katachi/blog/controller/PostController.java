package com.katachi.blog.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.model.Comment;
import com.katachi.blog.model.Post;
import com.katachi.blog.service.CommentService;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private PostService postService;

	@Autowired
	private MessageSource messageSource;

	/** 指定された記事が見つからなかった場合は 404 エラー画面を表示する */
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleNoResourceFound(ResourceNotFoundException e, Model model, Locale locale) {
		model.addAttribute("error", messageSource.getMessage("post.notfound", null, locale));
		model.addAttribute("status", HttpStatus.NOT_FOUND.value());

		return "error";
	}

	@GetMapping("/{slug}")
	public String show(Model model, @PathVariable String slug) {

		Post post = postService.getPostBySlug(slug);
		model.addAttribute(post);

		// 記事とコメントは別々に取得する
		List<Comment> comments = commentService.getByPostId(post.getId());
		model.addAttribute("comments", comments);

		return "posts/show";
	}

}
