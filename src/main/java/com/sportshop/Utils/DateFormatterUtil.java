package com.sportshop.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatterUtil {
    public static String formatDate(String dateTime) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime date = LocalDateTime.parse(dateTime, inputFormatter);

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("H:mm dd/MM/yyyy");
        return date.format(outputFormatter);
    }
}
