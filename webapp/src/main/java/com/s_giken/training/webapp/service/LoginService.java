package com.s_giken.training.webapp.service;

import java.util.List;

import com.s_giken.training.webapp.model.entity.Login;
import com.s_giken.training.webapp.model.entity.LoginSearchCondition;

// ログイン情報保存のサービスインターフェース
public interface LoginService {
    public void save(Login login);

    public List<Login> findByLatest(LoginSearchCondition loginSearchCondition);

    public List<Login> findByLast(LoginSearchCondition loginSearchCondition);
}
