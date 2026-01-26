package com.katachi.blog.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.form.PostForm;
import com.katachi.blog.model.Post;
import com.katachi.blog.model.User;
import com.katachi.blog.service.CategoryService;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;

	@GetMapping("/create")
	public String create(Model model, @ModelAttribute PostForm form) {
		model.addAttribute("categories", categoryService.getCategories());
		return "posts/create";
	}

	@PostMapping("/create")
	public String store(
		Model model,
		@AuthenticationPrincipal User user,
		@ModelAttribute @Validated PostForm form,
		BindingResult bindingResult
	) throws IOException {
		if (bindingResult.hasErrors()) {
			return create(model, form);
		}
		
		Post post = new Post();

		// form の値で post を更新
		post.setCategoryId(form.getCategoryId());
		post.setTitle(form.getTitle());
		post.setBody(form.getBody());

		// ログインユーザーを投稿者とする
		post.setUserId(user.getId());
		// スラッグは現在日時とする
		post.setSlug(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));
		// 投稿・更新日時とも現在日時に設定
		post.setPostedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		
		postService.post(post);

		return "redirect:/";
	}

}
