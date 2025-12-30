package com.katachi.blog.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.katachi.blog.exception.ResourceNotFoundException;
import com.katachi.blog.form.PostForm;
import com.katachi.blog.model.Post;
import com.katachi.blog.model.User;
import com.katachi.blog.service.CategoryService;
import com.katachi.blog.service.PostService;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private PostService postService;

	@Autowired
	private MessageSource messageSource;
	
	@Value("${app.media.directory}")
	private String mediaDirectory;

	/** 引数の Post を Form の内容で更新し、返す */
	private Post formToPost(PostForm form, Post post) throws IOException {

		post.setCategoryId(form.getCategoryId());
		post.setTitle(form.getTitle());
		post.setBody(form.getBody());

		MultipartFile thumbnailFile = form.getThumbnailFile();
		if (form.isDeleteThumbnail()) {
			// 画像を削除
			post.setThumbnail(null);
		}
		if (thumbnailFile != null && !thumbnailFile.getOriginalFilename().isBlank()) {
			// 画像を更新
			String originalFilename = thumbnailFile.getOriginalFilename();
			String extension = StringUtils.getFilenameExtension(originalFilename);
			// アップロードファイルはUUIDを使って重複しない名前にする
			String fileName = UUID.randomUUID().toString() + "." + extension;
			// 画像保存先フォルダに保存する
			Path destPath = Paths.get(mediaDirectory, fileName);
			// 保存先ディレクトリがなければ作成する
			Files.createDirectories(destPath.getParent());
			// アップロードしたファイルを保存
			Files.write(destPath, thumbnailFile.getBytes());
			// thumbnail にはファイル名を保存
			post.setThumbnail(fileName);
		}
		
		return post;
	}

	/** 指定された記事が見つからなかった場合は 404 エラー画面を表示する */
	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleNoResourceFound(ResourceNotFoundException e, Model model, Locale locale) {
		model.addAttribute("error", messageSource.getMessage("post.notfound", null, locale));
		model.addAttribute("status", HttpStatus.NOT_FOUND.value());

		return "error";
	}
	
	@GetMapping
	public String index(Model model,
			HttpServletRequest request,
			@AuthenticationPrincipal User user,
			@PageableDefault(page=0, size=10, sort="slug", direction=Direction.DESC) Pageable pageable
	) {
		// ログインユーザーの記事のみ
		Page<Post> page = postService.getPosts(
				Optional.empty(),
				Optional.empty(),
				Optional.of(user.getId()),
				pageable);
		model.addAttribute("page", page);
		
		// ページネーション用に URL をセット
		model.addAttribute("currentUrl", request.getRequestURI());

		// Thymeleaf で簡潔に扱えるように posts はここで取り出しておく
		List<Post> posts = page.getContent();
		model.addAttribute("posts", posts);

		return "posts/index";
	}

	@GetMapping("/create")
	public String create(Model model, @ModelAttribute PostForm form) {
		model.addAttribute("categories", categoryService.getCategories());
		return "posts/create";
	}

	@PostMapping("/create")
	public String store(Model model,
			@AuthenticationPrincipal User user,
			@ModelAttribute @Validated PostForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Locale locale
	) throws IOException {
		if (bindingResult.hasErrors()) {
			return create(model, form);
		}
		
		Post post = new Post();
		
		// ログインユーザーを投稿者とする
		post.setUserId(user.getId());
		// スラッグは現在日時とする
		post.setSlug(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));

		post = formToPost(form, post);
		postService.post(post);

		Object[] args = { post.getTitle() };
		redirectAttributes.addFlashAttribute("success",
			messageSource.getMessage("post.created", args, locale)
		);

		return "redirect:/";
	}

	@GetMapping("{id}/edit")
	public String edit(Model model, @PathVariable Integer id) {

		Post post = postService.getMyPostById(id);
		
		// バリデーションエラー時でなければ form の初期値を登録
		if (!model.containsAttribute("postForm")) {
			model.addAttribute("postForm", new PostForm(post));
		}
		
		model.addAttribute("categories", categoryService.getCategories());

		return "posts/edit";
	}
	
	@PostMapping("{id}/edit")
	public String update(Model model, @PathVariable Integer id,
		@ModelAttribute @Validated PostForm form,
		BindingResult bindingResult,
		HttpServletRequest request,
		RedirectAttributes redirectAttributes,
		Locale locale
	) throws IOException {
		Post post = postService.getMyPostById(id);
		
		if (bindingResult.hasErrors()) {
			return edit(model, id);
		}

		post = formToPost(form, post);
		postService.post(post);

		Object[] args = { post.getTitle() };
		redirectAttributes.addFlashAttribute("success",
			messageSource.getMessage("post.updated", args, locale)
		);

		return "redirect:" + request.getRequestURI();
	}
	
	@PostMapping("/delete")
	public String destroy(Model model, @RequestParam Integer id,
		RedirectAttributes redirectAttributes,
		Locale locale
	) throws IOException {
		Post post = postService.getMyPostById(id);
		postService.delete(post);

		Object[] args = { post.getTitle() };
		redirectAttributes.addFlashAttribute("success",
			messageSource.getMessage("post.deleted", args, locale)
		);

		return "redirect:/admin/posts";
	}
}
