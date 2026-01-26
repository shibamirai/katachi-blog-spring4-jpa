package com.katachi.blog.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.form.CommentForm;
import com.katachi.blog.model.Comment;
import com.katachi.blog.service.CommentService;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;

@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@HxRequest
	@GetMapping("{commentId}")
	String show(@PathVariable Integer commentId, Model model) {

		Comment comment = commentService.getMyComment(commentId);
		model.addAttribute("comment", comment);

		model.addAttribute("commentId", commentId);
		return "components/comment :: show";
	}

	@HxRequest
	@GetMapping("{commentId}/edit")
	String edit(@PathVariable Integer commentId, Model model) {

		if (model.getAttribute("commentForm") == null) {
			Comment comment = commentService.getMyComment(commentId);
			CommentForm form = new CommentForm();
			form.setBody(comment.getBody());
			model.addAttribute("commentForm", form);
		}

		model.addAttribute("commentId", commentId);
		return "components/comment :: edit";
	}

	@HxRequest
	@PatchMapping(produces=MediaType.TEXT_HTML_VALUE, path="/{commentId}")
	String update(
		@PathVariable Integer commentId,
		@ModelAttribute @Validated CommentForm form,
		BindingResult bindingResult, Model model
	) {
		if (bindingResult.hasErrors()) {
			return edit(commentId, model);
		}
		
		Comment comment = commentService.getMyComment(commentId);
		comment.setBody(form.getBody());
		// 更新日時を現在日時に設定
		comment.setUpdatedAt(LocalDateTime.now());

		commentService.update(comment);

		return show(commentId, model);
	}

}
