package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.AuthResponseDto;
import com.smartbiz.smartbiz_api.dto.UserDto;

public interface AuthService {

     String register(UserDto userDto);

     AuthResponseDto login(AuthDto authDto);


}
