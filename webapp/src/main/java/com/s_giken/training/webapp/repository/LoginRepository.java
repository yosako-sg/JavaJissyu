package com.s_giken.training.webapp.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.s_giken.training.webapp.model.entity.Login;

public interface LoginRepository extends JpaRepository<Login, LocalDateTime> {
        @Query(value = "select max(login_datetime) from t_login",
                        nativeQuery = true)
        public Optional<LocalDateTime> findByLoginDateTime(LocalDateTime loginDateTime);

        @Query(value = "select max(login_datetime) from t_login where login_datetime < (select max(login_datetime) from t_login)",
                        nativeQuery = true)
        public Optional<LocalDateTime> findByLastLoginDateTime(LocalDateTime loginDateTime);
}
