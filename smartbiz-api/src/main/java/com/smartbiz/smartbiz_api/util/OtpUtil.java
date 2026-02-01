package com.smartbiz.smartbiz_api.util;

import java.util.Random;

public class OtpUtil {

    public static String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }


}


