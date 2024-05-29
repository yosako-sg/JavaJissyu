package com.s_giken.training.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.s_giken.training.webapp.model.entity.LoginSearchCondition;
import com.s_giken.training.webapp.service.LoginService;

/**
 * ルートパスのコントローラークラス
 */
@Controller
@RequestMapping("/")
public class RootController {
	private LoginService loginService;

	public RootController(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * ルートパスにアクセスされた場合の処理
	 * 
	 * @return トップ画面のテンプレート名
	 */
	@GetMapping("/")
	public String hello(
			LoginSearchCondition loginSearchCondition,
			Model model) {
		var loginDateTime = loginService.findLatest(loginSearchCondition);
		var lastLoginDateTime = loginService.findLast(loginSearchCondition);

		model.addAttribute("loginDateTime", loginDateTime);
		model.addAttribute("lastLoginDateTime", lastLoginDateTime);

		/*
		 * if (CollectionUtils.isEmpty(lastLoginDateTime)) {
		 * model.addAttribute("lastLoginDateTime", "前回のログインがありません。");
		 * model.addAttribute("loginDateTime", loginDateTime);
		 * 
		 * return "top";
		 * }
		 */

		return "top";
	}

	/**
	 * ログイン画面を表示する
	 * 
	 * @return ログイン画面のテンプレート名
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
