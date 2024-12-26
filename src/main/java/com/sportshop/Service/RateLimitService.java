package com.sportshop.Service;

public interface RateLimitService {
    boolean isBlocked(String key); // Kiểm tra xem người dùng bị block chưa
    void recordAttempt(String key); // Ghi nhận một lần thử
    void resetAttempts(String key); // Reset số lần thử
}
