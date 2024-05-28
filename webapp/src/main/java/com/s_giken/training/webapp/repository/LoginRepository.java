package com.s_giken.training.webapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.s_giken.training.webapp.model.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Integer> {
    public List<Login> findFirstByLoginDateTimeOrderByLogindatetimeDesc(Date logindatetime);

    public List<Login> findFirstByLoginDateTimeLessThanfindFirstByLoginDateTimeOrderByLogindatetimeDescOrderByLogindatetimeDesc(
            Date logindatetime);
}
