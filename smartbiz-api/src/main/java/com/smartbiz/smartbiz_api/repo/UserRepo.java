package com.smartbiz.smartbiz_api.repo;

import com.smartbiz.smartbiz_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Search admins by name (case-insensitive)
    List<User> findByRoleIgnoreCaseAndNameContainingIgnoreCase(String role, String name);

}
