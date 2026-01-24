package com.katachi.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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
	public String index(Model model,
		@PageableDefault(page=0, size=6, sort="postedAt", direction=Direction.DESC) Pageable pageable
	) {

		Page<Post> page = postService.getPosts(pageable);
		model.addAttribute("page", page);

		// Thymeleaf で簡潔に扱えるように posts はここで取り出しておく
		List<Post> posts = page.getContent();
		model.addAttribute("posts", posts);

		return "index";
	}

}
