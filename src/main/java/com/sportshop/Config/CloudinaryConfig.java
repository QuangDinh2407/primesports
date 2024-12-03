package com.sportshop.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dcnxyqrj5",
                "api_key", "263821984546731",
                "api_secret", "CNeFFYtughh70Seb9qSY5w1Kmm8"
        );
        return new Cloudinary(config);
    }
}
