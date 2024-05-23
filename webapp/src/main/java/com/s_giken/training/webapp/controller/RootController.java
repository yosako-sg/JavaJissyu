package com.s_giken.training.webapp.controller;

import jakarta.servlet.http.HttpSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * ルートパスのコントローラークラス
 */
@Controller
@RequestMapping("/")
public class RootController {
	private HttpSession session;

	public RootController(HttpSession session) {
		this.session = session;
	}

	/**
	 * ルートパスにアクセスされた場合の処理
	 * 
	 * @return トップ画面のテンプレート名
	 */
	@GetMapping("/")
	public String hello(Model model) throws ParseException {
		// ログインした時の日時を収得し、Thymeleafに渡す
		long createTime = session.getCreationTime();
		Date createDate = new Date(createTime);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
		fmt.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		var loginDateTime = fmt.format(createDate);
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
