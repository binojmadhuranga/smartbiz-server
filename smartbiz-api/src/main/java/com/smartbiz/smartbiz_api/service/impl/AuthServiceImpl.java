package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.*;
import com.smartbiz.smartbiz_api.entity.PasswordResetOtp;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.event.UserRegisteredEvent;
import com.smartbiz.smartbiz_api.exception.BadRequestException;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.exception.UnauthorizedException;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.AuthService;
import com.smartbiz.smartbiz_api.service.EmailService;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import com.smartbiz.smartbiz_api.util.OtpUtil;
import com.smartbiz.smartbiz_api.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.smartbiz.smartbiz_api.repo.PasswordResetOtpRepo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordResetOtpRepo passwordResetOtpRepo;
    private final EmailService emailService;

    @Override
    @Transactional
    public String register(UserDto userDto) {
        String hashedPassword = PasswordUtil.hash(userDto.getPassword());

        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already registered");
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(hashedPassword); // Consider hashing the password
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole()); // Assuming UserDto has a role field
        userRepo.save(user);
        eventPublisher.publishEvent(new UserRegisteredEvent(user));
        return "User registered successfully!";
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponseDto login(AuthDto request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));


        boolean passwordMatch = PasswordUtil.matches(request.getPassword(), user.getPassword());

        if (!passwordMatch) {
            throw new UnauthorizedException("Invalid credentials");
        }
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponseDto(token, user.getRole(), user.getName());

    }

    @Transactional
    @Override
    public String forgotPassword(ForgotPasswordDto dto) {

        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() ->   new NotFoundException("User not found"));

        // Remove old OTPs for safety
        passwordResetOtpRepo.deleteByEmail(user.getEmail());

        String otp = OtpUtil.generateOtp();

        PasswordResetOtp resetOtp = PasswordResetOtp.builder()
                .email(user.getEmail())
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .build();

        passwordResetOtpRepo.save(resetOtp);

        // Send OTP via email (async)
        emailService.sendOtpEmail(user.getEmail(), otp);

        return "OTP sent to registered email";
    }

    @Transactional
    @Override
    public String resetPassword(ResetPasswordDto dto) {

        PasswordResetOtp resetOtp = passwordResetOtpRepo
                .findByEmailAndOtp(dto.getEmail(), dto.getOtp())
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));

        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired");
        }

        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setPassword(PasswordUtil.hash(dto.getNewPassword()));
        userRepo.save(user);

        // Delete OTP after successful reset
        passwordResetOtpRepo.deleteByEmail(dto.getEmail());

        return "Password reset successfully";
    }


}




