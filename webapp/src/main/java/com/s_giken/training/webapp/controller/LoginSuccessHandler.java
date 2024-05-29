package com.s_giken.training.webapp.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.s_giken.training.webapp.service.LoginService;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private LoginService loginService;

    // コンストラクタを作成
    public LoginSuccessHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        loginService.save();

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
