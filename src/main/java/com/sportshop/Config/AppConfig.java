package com.sportshop.Config;

import com.sportshop.Utils.CurrencyFormatterUtil;
import com.sportshop.Utils.DateFormatterUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CurrencyFormatterUtil currencyFormatter() {
        return new CurrencyFormatterUtil();
    }

    @Bean
    public DateFormatterUtil dateFormatter() {
        return new DateFormatterUtil();
    }
}
