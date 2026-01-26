package com.katachi.blog.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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

	@Value("${app.media.directory}")
	private String mediaDirectory;

	@GetMapping("/create")
	public String create(Model model, @ModelAttribute PostForm form) {
		model.addAttribute("categories", categoryService.getCategories());
		return "posts/create";
	}

	@PostMapping("/create")
	public String store(
		Model model,
		@AuthenticationPrincipal User user,
		@ModelAttribute @Validated PostForm form,
		BindingResult bindingResult
	) throws IOException {
		if (bindingResult.hasErrors()) {
			return create(model, form);
		}
		
		Post post = new Post();

		// form の値で post を更新
		post.setCategoryId(form.getCategoryId());
		post.setTitle(form.getTitle());
		post.setBody(form.getBody());
		MultipartFile thumbnailFile = form.getThumbnailFile();
		if (thumbnailFile == null || thumbnailFile.getOriginalFilename().isBlank()) {
			// ファイルがセットされていなければもとの thumbnail のまま(新規の場合は null)
			post.setThumbnail(form.getThumbnail());
		} else {
			// ファイルの拡張子を取得
			String originalFilename = thumbnailFile.getOriginalFilename();
			String extension = StringUtils.getFilenameExtension(originalFilename);
			// アップロードファイルはUUIDを使って重複しない名前に変更する
			String fileName = UUID.randomUUID().toString() + "." + extension;
			// 画像保存先
			Path destPath = Paths.get(mediaDirectory, fileName);
			// 保存先ディレクトリがなければ作成する
			Files.createDirectories(destPath.getParent());
			// アップロードしたファイルを保存
			Files.write(destPath, thumbnailFile.getBytes());
			// thumbnail にはファイル名を保存
			post.setThumbnail(fileName);
		}

		// ログインユーザーを投稿者とする
		post.setUserId(user.getId());
		// スラッグは現在日時とする
		post.setSlug(LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));
		// 投稿・更新日時とも現在日時に設定
		post.setPostedAt(LocalDateTime.now());
		post.setUpdatedAt(LocalDateTime.now());
		
		postService.post(post);

		return "redirect:/";
	}

}
