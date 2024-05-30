package com.s_giken.training.webapp.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.s_giken.training.webapp.model.entity.LoginSearchCondition;

// ログイン情報保存のサービスインターフェース
public interface LoginService {
    public void save();

    public Optional<LocalDateTime> findLatest(LoginSearchCondition loginSearchCondition);

    public Optional<LocalDateTime> findLast(LoginSearchCondition loginSearchCondition);
}
