package io.github.rothschil.common.config.cache.caffeine;


import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;

public class CaffeineExpiry implements Expiry<String, CaffeineEntry> {

    @Override
    public long expireAfterCreate(@NonNull String key, @NonNull CaffeineEntry value, long currentTime) {
        return value.getExpireTime();
    }

    @Override
    public long expireAfterUpdate(@NonNull String key, @NonNull CaffeineEntry value, long currentTime, @NonNegative long currentDuration) {
        return value.getExpireTime();
    }

    @Override
    public long expireAfterRead(@NonNull String key, @NonNull CaffeineEntry value, long currentTime, @NonNegative long currentDuration) {
        if (value.isAccessFresh()) {
            return value.getExpireTime();
        }
        return 11;
    }
}