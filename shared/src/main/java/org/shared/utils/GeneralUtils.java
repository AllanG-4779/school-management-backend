package org.shared.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class GeneralUtils {

    public static String generateOTP() {
        String digits = "0123456789";
        Random random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(digits.length());
            stringBuilder.append(digits.charAt(index));
        }
        return stringBuilder.toString();
    }
}
