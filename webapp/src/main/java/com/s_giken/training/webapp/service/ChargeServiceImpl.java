package com.s_giken.training.webapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.s_giken.training.webapp.model.entity.Charge;
import com.s_giken.training.webapp.model.entity.ChargeSearchCondition;
import com.s_giken.training.webapp.repository.ChargeRepository;

@Service
public class ChargeServiceImpl implements ChargeService {
    private ChargeRepository chargeRepository;

    // 料金管理機能のサービスクラスのコンストラクタ
    public ChargeServiceImpl(ChargeRepository chargeRepository) {
        this.chargeRepository = chargeRepository;
    }

    // 料金情報を全件取得する
    @Override
    public List<Charge> findAll() {
        return chargeRepository.findAll();
    }

    // 料金情報を1件取得する
    @Override
    public Optional<Charge> findById(int chargeId) {
        return chargeRepository.findById(chargeId);
    }

    //料金名を条件検索する
    @Override
    public List<Charge> findByConditions(ChargeSearchCondition chargeSearchCondition) {
        Sort sort = Sort.by(Sort.Direction.fromString(chargeSearchCondition.getSort()),
                chargeSearchCondition.getCategory());
        return chargeRepository.findByNameLike("%" + chargeSearchCondition.getName() + "%", sort);
    }

    // 料金情報を登録する
    @Override
    public void save(Charge charge) {
        chargeRepository.save(charge);
    }

    // 料金情報を更新する
    @Override
    public void deleteById(int chargeId) {
        chargeRepository.deleteById(chargeId);
    }
}
