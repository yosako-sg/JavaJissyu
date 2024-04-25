package com.s_giken.training.webapp.model.entity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "T_CHARGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "charge_id")
    private int chargeId;

    @Column(name = "name")
    @NotNull
    @NotBlank
    private String name;

    @Column(name = "amount")
    @NotNull
    private int amount;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @CreatedDate
    private Timestamp createdDate;

    @LastModifiedDate
    private Timestamp updatedDate;
}
