package com.sportshop.Service.Iml;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.sportshop.Service.RateLimitService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private static final int MAX_ATTEMPTS = 2; // Số lần thử tối đa
    private static final long BLOCK_TIME = 10 * 60 * 1000; // Thời gian chặn (10 phút)

    // Sử dụng Caffeine để quản lý số lần thử và reset sau khoảng thời gian nhất định
    private final Cache<String, AtomicInteger> attemptsCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES) // Reset sau 1 phút
            .maximumSize(1000) // Giới hạn số lượng entry trong cache
            .build();

    // Sử dụng cache để theo dõi thời gian chặn
    private final Cache<String, Long> blockTimeCache = Caffeine.newBuilder()
            .expireAfterWrite(BLOCK_TIME, TimeUnit.MILLISECONDS) // Cache sẽ hết hạn sau BLOCK_TIME
            .maximumSize(1000)
            .build();

    @Override
    public boolean isBlocked(String key) {
        AtomicInteger attempts = attemptsCache.getIfPresent(key);
        // Kiểm tra nếu số lần thử >= MAX_ATTEMPTS và kiểm tra thời gian chặn
        if (attempts != null && attempts.get() >= MAX_ATTEMPTS) {
            Long blockTime = blockTimeCache.getIfPresent(key);
            if (blockTime != null && System.currentTimeMillis() - blockTime < BLOCK_TIME) {
                // Nếu thời gian chặn chưa hết, người dùng bị chặn
                return true;
            }
            // Nếu hết thời gian chặn, xóa cache thời gian chặn
            blockTimeCache.invalidate(key);
        }
        return false;
    }

    @Override
    public void recordAttempt(String key) {
        AtomicInteger attempts = attemptsCache.get(key, k -> new AtomicInteger(0));
        int attemptCount = attempts.incrementAndGet(); // Tăng số lần thử

        // Nếu đạt số lần thử tối đa, chặn người dùng và lưu thời gian chặn
        if (attemptCount >= MAX_ATTEMPTS) {
            blockTimeCache.put(key, System.currentTimeMillis());
        }
    }

    @Override
    public void resetAttempts(String key) {
        // Xóa key khỏi cache
        attemptsCache.invalidate(key);
        blockTimeCache.invalidate(key);
    }
}
