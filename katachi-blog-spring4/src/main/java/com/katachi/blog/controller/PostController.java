package com.katachi.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.model.Post;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {

	@Autowired
	private PostService postService;

	/** 指定された記事が見つからなかった場合はエラー画面を表示する */
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleNoResourceFound(ResourceNotFoundException e, Model model) {
		model.addAttribute("error", "");
		model.addAttribute("message", "指定された記事が見つかりません");
		model.addAttribute("status", HttpStatus.NOT_FOUND);

		return "error";
	}

	@GetMapping("/{slug}")
	public String show(Model model, @PathVariable String slug) {

		Post post = postService.getPostBySlug(slug);
		model.addAttribute(post);

		return "posts/show";
	}

}
