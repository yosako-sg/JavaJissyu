package com.s_giken.training.webapp.service;

// ログイン情報保存のサービスインターフェース
public interface LoginService {
    public void saveLoginDateTime();

    public String selectLatestLoginDateTime();

    public String selectLastLoginDateTime();
}
