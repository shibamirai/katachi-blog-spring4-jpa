package com.katachi.blog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.katachi.blog.model.Category;
import com.katachi.blog.model.Post;
import com.katachi.blog.service.CategoryService;
import com.katachi.blog.service.PostCriteria;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;

	@ModelAttribute("categories")
	List<Category> categories() {
		return categoryService.getCategories();
	}

	@GetMapping
	public String index(Model model,
		@RequestParam Optional<String> search,
		@RequestParam Optional<Integer> category,
		@PageableDefault(page=0, size=6, sort="postedAt", direction=Direction.DESC) Pageable pageable
	) {

		PostCriteria postCriteria = new PostCriteria(search, category);
		Page<Post> page = postService.getPosts(postCriteria, pageable);
		model.addAttribute("page", page);

		// Thymeleaf で簡潔に扱えるように posts はここで取り出しておく
		List<Post> posts = page.getContent();
		model.addAttribute("posts", posts);

		// ページネーションのリンク用に page 以外のクエリーを組み立てて渡す
		List<String> params = new ArrayList<>();
		if (search.isPresent() && !search.get().isBlank()) params.add("search=" + search.get());
		if (category.isPresent()) params.add("category=" + category.get());
		Optional<String> queries = params.stream().reduce(
				(accum, value) -> accum + "&" + value
		);
		model.addAttribute("queries", (queries.isPresent() ? "?" + queries.get() : ""));

		// カテゴリが選択されていればドロップダウン表示用にカテゴリ名をセットする
		category.ifPresent(id ->
			model.addAttribute("currentCategory", categoryService.getCategory(id).getName())
		);

		return "index";
	}

}
