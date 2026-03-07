package com.smartbiz.smartbiz_api.service.impl;

import com.smartbiz.smartbiz_api.dto.UserDto;
import com.smartbiz.smartbiz_api.entity.User;
import com.smartbiz.smartbiz_api.exception.NotFoundException;
import com.smartbiz.smartbiz_api.repo.UserRepo;
import com.smartbiz.smartbiz_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        return mapToDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id " + id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());

        User updatedUser = userRepo.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NotFoundException("User not found with id " + id);
        }
        userRepo.deleteById(id);
    }

    // Search admins by name
    @Override
    public List<UserDto> searchAdminsByName(String query) {
        String q = query == null ? "" : query.trim();
        return userRepo.findByRoleIgnoreCaseAndNameContainingIgnoreCase("ADMIN", q)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // === Mappers ===
    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getPlan()
        );
    }



}
