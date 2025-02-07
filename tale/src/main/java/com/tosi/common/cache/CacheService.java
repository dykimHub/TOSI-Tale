package com.tosi.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에서 캐시된 데이터를 조회하고, 지정된 타입으로 변환하여 반환합니다.
     *
     * @param key   조회할 캐시 키
     * @param type  반환할 객체의 클래스 타입
     * @param <T>   반환할 객체의 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     * @return      캐싱된 데이터가 존재하면 변환하여 반환하고, 없으면 null 반환
     */
    public <T> T getCachedDto(String key, Class<T> type) {
        Object cachedDto = redisTemplate.opsForValue().get(key);
        return type.cast(cachedDto);
    }

    /**
     * 해당 객체를 Redis 캐시에 저장합니다.
     *
     * @param key 캐시에 저장할 키
     * @param value 캐시에 저장할 객체
     * @param timeout 캐시 만료 시간
     * @param unit 만료 시간 단위
     * @param <T> 저장할 객체 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     */
    public <T> void setCachedDto(String key, T value, long timeout, TimeUnit unit) {
        /**
         * 제네릭 타입(T)은 컴파일 시 타입 정보가 소거(Type Erasure)되며,
         * redisTemplate이 사용하는 Object 타입으로 저장됩니다.
         */
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

}
