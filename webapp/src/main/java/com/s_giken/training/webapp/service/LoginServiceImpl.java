package com.s_giken.training.webapp.service;

import jakarta.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import com.s_giken.training.webapp.model.entity.Login;
import com.s_giken.training.webapp.model.entity.LoginSearchCondition;
import com.s_giken.training.webapp.repository.LoginRepository;

// ログイン情報保存のサービスクラス(実態クラス)

@Service
public class LoginServiceImpl implements LoginService {
    private HttpSession session;
    private LoginRepository loginRepository;

    // コンストラクタを作成
    public LoginServiceImpl(HttpSession session, LoginRepository loginRepository) {
        this.session = session;
        this.loginRepository = loginRepository;
    }

    // ログインした日時を保存
    @Override
    public void save(Login login) {
        long createTime = session.getCreationTime();
        Date createDate = new Date(createTime);
        loginRepository.save(login);
    }

    // 最新のログイン日時を収得
    @Override
    public List<Login> findByLatest(LoginSearchCondition loginSearchCondition) {
        return loginRepository.findFirstByLoginDateTimeOrderByLogindatetimeDesc(
                loginSearchCondition.getLogindatetime());
    }

    // 前回のログイン日時を収得
    @Override
    public List<Login> findByLast(LoginSearchCondition loginSearchCondition) {
        return loginRepository
                .findFirstByLoginDateTimeLessThanfindFirstByLoginDateTimeOrderByLogindatetimeDescOrderByLogindatetimeDesc(
                        loginSearchCondition.getLogindatetime());
    }
}
