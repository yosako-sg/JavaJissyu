package com.s_giken.training.webapp.service;

import com.s_giken.training.webapp.model.entity.LoginSearchCondition;

// ログイン情報保存のサービスインターフェース
public interface LoginService {
    public void save();

    public String findLatest(LoginSearchCondition loginSearchCondition);

    public String findLast(LoginSearchCondition loginSearchCondition);
}
