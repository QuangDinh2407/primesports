package com.sportshop.Repository;

import com.sportshop.Entity.PaymentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, String> {

    List<PaymentTypeEntity> findAll();

    PaymentTypeEntity findByName(String name);
}
