package com.s_giken.training.webapp.service;

import jakarta.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

// ログイン情報保存のサービスクラス(実態クラス)
@Service
public class LoginServiceImpl implements LoginService {
    // HttpsSession型とJdbcTemplate型のフィールドを定義
    private HttpSession session;
    private JdbcTemplate jdbcTemplate;

    // コンストラクタを作成
    public LoginServiceImpl(HttpSession session, JdbcTemplate jdbcTemplate) {
        this.session = session;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ログインした日時を保存
    @Override
    public void saveLoginDateTime() {
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
    }

    // 最新のログイン日時を収得
    @Override
    public String selectLatestLoginDateTime() {
        return jdbcTemplate.queryForObject(
                """
                        SELECT MAX(login_datetime)
                        FROM T_LOGIN_DATETIME
                        """, String.class);
    }

    // 前回のログイン日時を収得
    @Override
    public String selectLastLoginDateTime() {
        return jdbcTemplate.queryForObject(
                """
                        SELECT MAX(login_datetime)
                        FROM T_LOGIN_DATETIME
                        WHERE login_datetime < (SELECT MAX(login_datetime) FROM T_LOGIN_DATETIME)
                        """, String.class);
    }
}
