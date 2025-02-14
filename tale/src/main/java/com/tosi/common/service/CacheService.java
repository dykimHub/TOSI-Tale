package com.tosi.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에서 캐시 키를 조회하고, 지정된 타입으로 변환합니다.
     *
     * @param key  조회할 캐시 키
     * @param type 반환할 객체의 클래스 타입
     * @param <T>  반환할 객체의 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     * @return 캐싱된 데이터가 존재하면 변환하여 반환하고, 없으면 null 반환
     */
    public <T> T getCache(String key, Class<T> type) {
        Object cachedDto = redisTemplate.opsForValue().get(key);
        return type.cast(cachedDto);
    }

    /**
     * Redis에서 여러 개의 캐시 키를 조회하여 지정된 타입으로 변환합니다.
     *
     * @param keys 조회할 캐시 키 목록
     * @param type 반환할 객체의 클래스 타입
     * @param <T>  반환할 객체의 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     * @return 캐싱된 데이터 목록을 변환하여 반환하고, 없으면 빈 리스트 반환
     */
    public <T> List<T> getMultiCaches(List<String> keys, Class<T> type) {
        List<Object> cachedDtos = redisTemplate.opsForValue().multiGet(keys);
        return cachedDtos.stream()
                .map(type::cast)
                .toList();
    }

    /**
     * 해당 객체를 Redis 캐시에 저장합니다.
     *
     * @param key     캐시에 저장할 키
     * @param value   캐시에 저장할 객체
     * @param timeout 캐시 만료 시간
     * @param unit    만료 시간 단위
     * @param <T>     저장할 객체 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     */
    public <T> void setCache(String key, T value, long timeout, TimeUnit unit) {
        /**
         * 제네릭 타입(T)은 컴파일 시 타입 정보가 소거(Type Erasure)되며,
         * redisTemplate이 사용하는 Object 타입으로 저장됩니다.
         */
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 여러 객체를 Redis 캐시에 저장하고, 개별적으로 만료 시간을 설정합니다.
     *
     * @param cacheMap 캐시에 저장할 키-값(캐시 키 - 동화 객체)
     * @param timeout  캐시 만료 시간
     * @param unit     만료 시간 단위
     */
    public void setMultiCaches(Map<String, Object> cacheMap, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().multiSet(cacheMap);

        /**
         * MSET 명령은 만료 시간 설정 기능이 없습니다.
         * 저장된 각 캐시 키에 대해 개별적으로 expire 명령을 호출하여 만료 시간을 지정합니다.
         */
        for (String key : cacheMap.keySet()) {
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * 지정된 키에 해당하는 객체를 Redis 캐시에서 삭제합니다.
     *
     * @param key 캐시에 저장된 키
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

}
