package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.UserDto;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.AuthService;
import com.smartbiz.smartbiz_api.util.JwtUtil;
import com.smartbiz.smartbiz_api.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String register(UserDto userDto) {
        String hashedPassword = PasswordUtil.hash(userDto.getPassword());
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            return "Email already registered!";
        }

        User user = new User();
        user.setName(userDto.getName());
        user.setPassword(hashedPassword); // Consider hashing the password
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole()); // Assuming UserDto has a role field
        userRepo.save(user);
        return "User registered successfully!";
    }

    public String login(AuthDto request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

//        if (!user.getPassword().equals(request.getPassword())) {
//            throw new RuntimeException("Invalid credentials");
//        }

        boolean passwordMatch = PasswordUtil.matches(request.getPassword(), user.getPassword());

        if (!passwordMatch) {
            throw new RuntimeException("Invalid password");
        }
        // Generate JWT token
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }


}




