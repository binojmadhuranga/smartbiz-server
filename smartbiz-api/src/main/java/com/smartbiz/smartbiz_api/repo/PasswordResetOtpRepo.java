package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOtpRepo extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndOtp(String email, String otp);
    void deleteByEmail(String email);


}
