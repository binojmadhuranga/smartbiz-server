package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}

