package com.s_giken.training.webapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "T_LOGIN")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    @Id
    @Column(name = "login_datetime")
    private LocalDateTime loginDateTime;
}
