package com.s_giken.training.webapp.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import com.s_giken.training.webapp.model.entity.Charge;

public interface ChargeRepository extends JpaRepository<Charge, Integer> {
    public List<Charge> findByNameLike(String name);

    public List<Charge> findAll(String category, Sort sort);
}
