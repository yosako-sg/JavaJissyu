package com.s_giken.training.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
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
	 * .get().format(formatter)
	 * 
	 * @return トップ画面のテンプレート名
	 */
	@GetMapping("/")
	public String hello(
			LoginSearchCondition loginSearchCondition,
			Model model) {
		var loginDateTime = loginService.findLatest(loginSearchCondition);
		var lastLoginDateTime = loginService.findLast(loginSearchCondition);

		if (ObjectUtils.isEmpty(lastLoginDateTime)) {
			model.addAttribute("loginDateTime", loginDateTime);
			model.addAttribute("lastLoginDateTime", "前回のログインがありません。");

			return "top";
		}

		model.addAttribute("loginDateTime", loginDateTime);
		model.addAttribute("lastLoginDateTime", lastLoginDateTime);

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
