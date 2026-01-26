package com.katachi.blog.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.katachi.blog.form.CommentForm;
import com.katachi.blog.model.Comment;
import com.katachi.blog.service.CommentService;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxLocationRedirectView;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;

@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private MessageSource messageSource;

	/* 指定されたコメントが見つからない場合 404 エラー画面を表示 */
	@ExceptionHandler(NoSuchElementException.class)
	public HtmxLocationRedirectView handleNoResourceFound(
		NoSuchElementException e,
		RedirectAttributes redirectAttributes,
		Locale locale
	) {
		redirectAttributes.addFlashAttribute("errorMessage",
			messageSource.getMessage("comment.notfound", null, locale)
		);
		return new HtmxLocationRedirectView("/error/404");
	}

	/* 指定されたコメントの編集権限がない場合 */
	@ExceptionHandler(AccessDeniedException.class)
	public HtmxLocationRedirectView handleAccessDenied(
		AccessDeniedException e,
		RedirectAttributes redirectAttributes,
		Locale locale
	) {
		redirectAttributes.addFlashAttribute("errorMessage",
			messageSource.getMessage("comment.notauthorized", null, locale)
		);
		return new HtmxLocationRedirectView("/error/403");
	}

	/* その他の例外は 500 エラー画面を表示 */
	@ExceptionHandler(Exception.class)
	public HtmxLocationRedirectView handleException(Exception e) {
		return new HtmxLocationRedirectView("/error/500");
	}

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
	List<ModelAndView> update(
		@PathVariable Integer commentId,
		@ModelAttribute @Validated CommentForm form,
		BindingResult bindingResult,
		Model model,
		Locale locale
	) {
		if (bindingResult.hasErrors()) {
			return List.of(
				new ModelAndView(edit(commentId, model))
			);
		}
		
		Comment comment = commentService.getMyComment(commentId);
		comment.setBody(form.getBody());
		// 更新日時を現在日時に設定
		comment.setUpdatedAt(LocalDateTime.now());

		commentService.update(comment);

		model.addAttribute("htmxMessage",
			messageSource.getMessage("comment.updated", null, locale)
		);

		return List.of(
			new ModelAndView(show(commentId, model)),
			new ModelAndView("components/flash :: htmx-flash")
		);
	}

	@HxRequest
	@DeleteMapping(produces=MediaType.TEXT_HTML_VALUE, path="/{commentId}")
	List<ModelAndView> delete(
			@PathVariable Integer commentId,
			Model model,
			Locale locale
	) {
		Comment comment = commentService.getMyComment(commentId);
		commentService.delete(comment);

		List<Comment> comments = commentService.getByPostId(comment.getPostId());
		model.addAttribute("comments", comments);

		model.addAttribute("htmxMessage",
			messageSource.getMessage("comment.deleted", null, locale)
		);

		return List.of(
			new ModelAndView("posts/show :: comment-list"),
			new ModelAndView("components/flash :: htmx-flash")
		);
	}

}
