package com.sportshop.Utils;

import java.security.SecureRandom;

public class randomStringUtil {

    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>?";
    private static final String ALL_CHARS = LOWER_CASE + UPPER_CASE + DIGITS + SPECIAL_CHARS;

    private static final SecureRandom random = new SecureRandom();


    public static String randomOTP (int otp_length)
    {
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otp_length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public static String randomPassword(int length)
    {
        StringBuilder password = new StringBuilder(length);

        // Bắt buộc có ít nhất một ký tự từ mỗi nhóm
        password.append(LOWER_CASE.charAt(random.nextInt(LOWER_CASE.length())));
        password.append(UPPER_CASE.charAt(random.nextInt(UPPER_CASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Tạo các ký tự còn lại ngẫu nhiên từ tất cả các nhóm
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }

        // Trộn mật khẩu để không theo thứ tự cố định của các ký tự đầu
        return shuffleString(password.toString());
    }

    public static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }

//    public static void main(String[] args) {
//        String a=randomOTP(4);
//        System.out.println( a );
//    }


}
