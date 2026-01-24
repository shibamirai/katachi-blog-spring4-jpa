package com.katachi.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.katachi.blog.model.Category;
import com.katachi.blog.model.Comment;
import com.katachi.blog.model.Post;
import com.katachi.blog.model.User;
import com.katachi.blog.repository.CategoryRepository;
import com.katachi.blog.repository.CommentRepository;
import com.katachi.blog.repository.PostRepository;
import com.katachi.blog.repository.UserRepository;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CommentRepository commentRepository;
	
	@GetMapping
	public String index(Model model) {

		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		List<User> users = userRepository.findAll();
		model.addAttribute("users", users);
		List<Post> posts = postRepository.findAll();
		model.addAttribute("posts", posts);
		List<Comment> comments = commentRepository.findAll();
		model.addAttribute("comments", comments);

		return "index";
	}

}
