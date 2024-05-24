package com.s_giken.training.webapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
// import org.springframework.ui.Model;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    // HttpsSession型とJdbcTemplate型のフィールドを定義
    private HttpSession session;
    private JdbcTemplate jdbcTemplate;

    // コンストラクタを作成
    public LoginSuccessHandler(HttpSession session, JdbcTemplate jdbcTemplate) {
        this.session = session;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // ログインした時の日時を収得
        long createTime = session.getCreationTime();
        Date createDate = new Date(createTime);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
        fmt.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        var loginDateTime = fmt.format(createDate);
        // DBに保存
        jdbcTemplate.update(
                """
                        INSERT INTO T_LOGIN_DATETIME (
                            login_datetime
                        ) VALUES (
                            ?
                        )""", loginDateTime);
        //model.addAttribute("loginDateTime", loginDateTime);
    }
}
