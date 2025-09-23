package com.smartbiz.smartbiz_api.service;



import com.smartbiz.smartbiz_api.dto.UserDto;

import java.util.List;


public interface UserService {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);

    // Search admins by name (case-insensitive contains)
    List<UserDto> searchAdminsByName(String query);

}
