package com.katachi.blog.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.katachi.blog.form.RegisterForm;
import com.katachi.blog.model.User;
import com.katachi.blog.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public String get(Model model, @ModelAttribute RegisterForm form) {
		return "auth/register";
	}

	@PostMapping
	public String post(
			Model model,
			@ModelAttribute @Validated RegisterForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Locale locale
	) {
		if (bindingResult.hasErrors()) {
			return get(model, form);
		}

		User user = new User();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));
		user.setRole("ROLE_GENERAL");

		user = userService.register(user);

		Object[] args = { user.getName() };
		redirectAttributes.addFlashAttribute("flashMessage",
			messageSource.getMessage("user.registered", args, locale)
		);
		
		return "redirect:/login";
	}

}
