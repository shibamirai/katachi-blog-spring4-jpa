package com.katachi.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MyErrorController {

	/**
	 * 指定された HTTP エラーコードに対応したエラー画面を表示する。
	 * アプリ内でエラー発生時にリダイレクトで表示させることを想定。
	 * @param statusCode
	 * @param model
	 * @return
	 */
	@GetMapping("/error/{statusCode}")
	public String error(@PathVariable int statusCode, Model model) {

		HttpStatus status = HttpStatus.valueOf(statusCode);
		String error = (String)model.getAttribute("errorMessage");
		model.addAttribute("error", error != null ? error : status.getReasonPhrase());
		model.addAttribute("status", status.value());

		return "error";
	}
}
