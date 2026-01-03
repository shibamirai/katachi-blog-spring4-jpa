package com.katachi.blog.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.form.CommentForm;
import com.katachi.blog.model.Comment;
import com.katachi.blog.service.CommentService;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxResponse;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;

@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageSource messageSource;

	/* 指定されたコメントが見つからない場合 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleNoResourceFound(ResourceNotFoundException e, Model model,
			HtmxRequest htmxRequest, HtmxResponse htmxResponse, Locale locale) {
		model.addAttribute("htmxError", messageSource.getMessage("comment.notfound", null, locale));
		htmxResponse.setRetarget("previous .error-message");
		return "components/comment :: message";
	}
	
	/* 指定されたコメントの編集権限がない場合 */
	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDenied(AccessDeniedException e, Model model,
			HtmxRequest htmxRequest, HtmxResponse htmxResponse, Locale locale) {
		model.addAttribute("htmxError", messageSource.getMessage("comment.notauthorized", null, locale));
		htmxResponse.setRetarget("previous .error-message");
		return "components/comment :: message";
	}

	@HxRequest
	@GetMapping("{commentId}/edit")
	String edit(@PathVariable Integer commentId, Model model) {

		if (model.getAttribute("commentForm") == null) {
			Comment comment = commentService.getMyComment(commentId);
			CommentForm form = new CommentForm(comment.getBody());
			model.addAttribute("commentId", commentId);
			model.addAttribute("commentForm", form);
		}

		return "components/comment :: edit";
	}

	@HxRequest
	@GetMapping("{commentId}")
	String show(@PathVariable Integer commentId, Model model) {

		Comment comment = commentService.getMyComment(commentId);
		model.addAttribute("commentId", commentId);
		model.addAttribute("comment", comment);

		return "components/comment :: show";
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
		commentService.post(comment);

		return show(commentId, model);
	}

	@HxRequest
	@DeleteMapping(produces=MediaType.TEXT_HTML_VALUE, path="/{commentId}")
	String delete(@PathVariable Integer commentId, Model model) {
		
		Comment comment = commentService.getMyComment(commentId);
		commentService.delete(comment);

		List<Comment> comments = commentService.getByPostId(comment.getPostId());
		model.addAttribute("comments", comments);
		return "posts/show :: comments";
	}
}
