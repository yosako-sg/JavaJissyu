package com.s_giken.training.webapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.s_giken.training.webapp.model.entity.Login;

public interface LoginRepository extends JpaRepository<Login, LocalDateTime> {
    @Query(value = "select login_datetime from t_login order by login_datetime desc limit 1",
            nativeQuery = true)
    public List<Login> findByLoginDateTime(LocalDateTime loginDateTime);
    //
    //select max(login_datetime) from t_login

    @Query(value = "select login_datetime from t_login where login_datetime < (select login_datetime from t_login order by login_datetime desc limit 1) order by login_datetime desc limit 1",
            nativeQuery = true)
    public List<Login> findByLastLoginDateTime(LocalDateTime loginDateTime);
}
