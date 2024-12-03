package com.sportshop.Utils;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class CurrencyFormatterUtil {
    public String formatCurrency(double amount) {
        Locale vietnamLocale = new Locale("vi", "VN");
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(vietnamLocale);
        return numberFormatter.format(amount) + " VNĐ";
    }
}
