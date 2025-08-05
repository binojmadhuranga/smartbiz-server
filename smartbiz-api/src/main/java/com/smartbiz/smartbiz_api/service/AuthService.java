package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.UserDto;

public interface AuthService {


    public String register( UserDto userDto);
    public String login(AuthDto authDto);



}
