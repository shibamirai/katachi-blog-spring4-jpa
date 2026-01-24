package com.katachi.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.model.Post;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private PostService postService;
	
	@GetMapping
	public String index(Model model) {

		List<Post> posts = postService.getPosts();
		model.addAttribute("posts", posts);

		return "index";
	}

}
