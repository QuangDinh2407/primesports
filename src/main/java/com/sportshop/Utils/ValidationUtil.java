package com.sportshop.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtil {
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return phone.matches("^(\\+84|0)\\d{9,10}$");
    }

    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) return false;
        return name.matches("^[\\p{L} .'-]+$");
    }

    public static boolean isBirthDateValid(Date birthDate) {
        if (birthDate == null) return false;
        Date currentDate = new Date();
        return birthDate.before(currentDate);
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidDate(String date, String pattern) {
        if (date == null || date.isEmpty()) return false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Hàm kiểm tra điều kiện mật khẩu mới
    public static boolean isValidPassword(String password) {
        // Regex kiểm tra điều kiện mật khẩu
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,}$";
        return password.matches(passwordRegex);
    }
}
