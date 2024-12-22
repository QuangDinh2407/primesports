package com.sportshop.Utils;

import com.sportshop.Contants.FormatDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDateUtil {

    // Phương thức chuyển đổi String thành Date với định dạng cụ thể
    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(FormatDate.FM_DATE_TIME);
        try {
            return formatter.parse(dateStr); // Chuyển đổi chuỗi sang Date
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: " + FormatDate.FM_DATE_TIME, e);
        }
    }

}

