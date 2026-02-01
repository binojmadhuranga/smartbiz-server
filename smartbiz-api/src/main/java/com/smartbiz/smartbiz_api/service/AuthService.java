package com.smartbiz.smartbiz_api.service;

import com.smartbiz.smartbiz_api.dto.AuthDto;
import com.smartbiz.smartbiz_api.dto.AuthResponseDto;
import com.smartbiz.smartbiz_api.dto.ForgotPasswordDto;
import com.smartbiz.smartbiz_api.dto.UserDto;
import com.smartbiz.smartbiz_api.dto.ResetPasswordDto;

public interface AuthService {

     String register(UserDto userDto);

     AuthResponseDto login(AuthDto authDto);

    String forgotPassword(ForgotPasswordDto dto);

    String resetPassword(ResetPasswordDto dto);
}
