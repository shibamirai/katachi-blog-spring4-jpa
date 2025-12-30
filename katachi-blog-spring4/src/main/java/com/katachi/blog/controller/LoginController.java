package com.katachi.blog.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String getLogin(Model model, HttpServletRequest request) {
		model.addAttribute("currentUrl", request.getRequestURI());
		return "auth/login";
	}
}
