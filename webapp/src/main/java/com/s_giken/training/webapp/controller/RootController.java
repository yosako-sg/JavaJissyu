package com.s_giken.training.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.s_giken.training.webapp.service.LoginService;
import io.micrometer.common.util.StringUtils;

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
	public String hello(Model model) {
		var lastLoginDateTime = loginService.selectLastLoginDateTime();
		var loginDateTime = loginService.selectLatestLoginDateTime();
		if (StringUtils.isEmpty(lastLoginDateTime)) {
			model.addAttribute("lastLoginDateTime", "前回のログインがありません。");
			model.addAttribute("loginDateTime", loginDateTime);

			return "top";
		}
		model.addAttribute("lastLoginDateTime", lastLoginDateTime);
		model.addAttribute("loginDateTime", loginDateTime);

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
