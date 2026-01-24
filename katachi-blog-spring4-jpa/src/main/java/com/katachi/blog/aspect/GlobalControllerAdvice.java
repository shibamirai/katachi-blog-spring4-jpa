package com.katachi.blog.aspect;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

	/** 現在の URL を Model にセットする */
	@ModelAttribute
	public void setCurrentURL(Model model, HttpServletRequest request) {
		model.addAttribute("currentUrl", request.getRequestURI());
	}
}
