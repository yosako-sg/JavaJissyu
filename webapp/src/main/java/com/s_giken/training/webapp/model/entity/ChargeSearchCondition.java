package com.s_giken.training.webapp.model.entity;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeSearchCondition {
    private String name;
    private String category;
    private Sort sort;
}
