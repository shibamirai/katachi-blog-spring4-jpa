package com.katachi.blog.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.katachi.blog.form.CommentForm;
import com.katachi.blog.model.Comment;
import com.katachi.blog.model.Post;
import com.katachi.blog.model.User;
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

	/** 指定された記事が見つからなかった場合はエラー画面を表示する */
	@ExceptionHandler(NoSuchElementException.class)
	public String handleNoResourceFound(NoSuchElementException e, Model model, Locale locale) {
		model.addAttribute("error", messageSource.getMessage("post.notfound", null, locale));
		model.addAttribute("status", HttpStatus.NOT_FOUND.value());

		return "error";
	}

	@GetMapping("/{slug}")
	public String show(Model model, @PathVariable String slug, @ModelAttribute CommentForm form) {

		Post post = postService.getPostBySlug(slug);
		model.addAttribute(post);

		List<Comment> comments = commentService.getByPostId(post.getId());
		model.addAttribute("comments", comments);

		return "posts/show";
	}

	@PostMapping("/{slug}/comments")
	public String store(Model model,
			@PathVariable String slug,
			@AuthenticationPrincipal User user,
			@ModelAttribute @Validated CommentForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Locale locale
	) {

		Post post = postService.getPostBySlug(slug);
		if (bindingResult.hasErrors()) {
			return show(model, slug, form);
		}

		Comment comment = new Comment();
		comment.setBody(form.getBody());

		// スラッグで指定された記事へのコメントとする
		comment.setPostId(post.getId());
		// ログインユーザーを投稿者とする
		comment.setUserId(user.getId());
		// 投稿・更新日時とも現在日時に設定
		comment.setPostedAt(LocalDateTime.now());
		comment.setUpdatedAt(LocalDateTime.now());

		commentService.post(comment);

		redirectAttributes.addFlashAttribute("flashMessage",
			messageSource.getMessage("comment.created", null, locale)
		);

		return "redirect:/posts/" + slug;
	}

}
