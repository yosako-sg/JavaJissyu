package com.s_giken.training.webapp.service;

import jakarta.servlet.http.HttpSession;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import com.s_giken.training.webapp.model.entity.Login;
import com.s_giken.training.webapp.model.entity.LoginSearchCondition;
import com.s_giken.training.webapp.repository.LoginRepository;

// ログイン情報保存のサービスクラス(実態クラス)
@Service
public class LoginServiceImpl implements LoginService {
    private HttpSession session;
    private LoginRepository loginRepository;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    // コンストラクタを作成
    public LoginServiceImpl(HttpSession session, LoginRepository loginRepository) {
        this.session = session;
        this.loginRepository = loginRepository;
    }

    // ログインした日時を保存
    @Override
    public void save() {
        // セッション開始時間をLocalDateTime型に変換
        long createTime = session.getCreationTime();
        LocalDateTime creatDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(createTime),
                ZoneId.of("Asia/Tokyo"));

        // Loginエンティティオブジェクトを作成し、変換したセッション開始時間を設定してセーブする
        Login loginInfo = new Login();
        loginInfo.setLoginDateTime(creatDate);
        loginRepository.save(loginInfo);
    }

    // 最新のログイン日時を収得
    @Override
    public String findLatest(LoginSearchCondition loginSearchCondition) {
        return loginRepository
                .findByLoginDateTime(loginSearchCondition.getLoginDateTime())
                .format(formatter);
    }

    // 前回のログイン日時を収得
    @Override
    public String findLast(LoginSearchCondition loginSearchCondition) {
        var lastLoginDateTime = loginRepository
                .findByLastLoginDateTime(loginSearchCondition.getLoginDateTime());

        if (lastLoginDateTime.isPresent()) {
            String lastLoginDateTimestr = lastLoginDateTime
                    .get()
                    .format(formatter);

            return lastLoginDateTimestr;
        }

        return null;
    }
}
