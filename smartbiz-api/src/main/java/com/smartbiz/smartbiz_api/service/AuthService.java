package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.AuthResponseDto;
import com.smartbiz.smartbiz_api.dto.UserDto;

public interface AuthService {


    public String register( UserDto userDto);
    public AuthResponseDto login(AuthDto authDto);



}
