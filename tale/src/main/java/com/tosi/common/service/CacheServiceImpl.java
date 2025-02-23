package com.tosi.common.service;

import com.tosi.common.constants.CachePrefix;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에서 캐시 키를 조회하고, 지정된 타입으로 변환합니다.
     *
     * @param key  조회할 캐시 키
     * @param type 반환할 객체의 클래스 타입
     * @param <T>  반환할 객체의 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     * @return 캐싱된 데이터가 존재하면 변환하여 반환하고, 없으면 Optional.empty() 반환
     */
    @Override
    public <T> Optional<T> getCache(String key, Class<T> type) {
        Object cachedDto = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(type.cast(cachedDto));
    }

    /**
     * Redis에서 여러 개의 캐시 키를 조회하여 지정된 타입으로 변환합니다.
     *
     * @param keys 조회할 캐시 키 목록
     * @param type 반환할 객체의 클래스 타입
     * @param <T>  반환할 객체의 타입 (메서드 호출 시 Class<T>에 따라 결정됨)
     * @return 캐싱된 데이터 목록을 변환하여 반환하고, 없으면 빈 리스트 반환
     */
    @Override
    public <T> List<T> getMultiCaches(List<String> keys, Class<T> type) {
        List<Object> cachedDtos = redisTemplate.opsForValue().multiGet(keys);
        return cachedDtos.stream()
                .filter(Objects::nonNull) // null 값(조회되지 않은 캐시) 제외
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
    @Override
    public <T> void setCache(String key, T value, long timeout, TimeUnit unit) {
        /**
         * 제네릭 타입(T)은 컴파일 시 타입 정보가 소거(Type Erasure)되며,
         * redisTemplate이 사용하는 Object 타입으로 저장됩니다.
         */
        redisTemplate.opsForValue().set(key, value, timeout, unit);
        log.info("Cache Set: {}, TTL: {} {}", key, timeout, unit);
    }

    /**
     * 여러 객체를 Redis 캐시에 저장하고, 개별적으로 만료 시간을 설정합니다.
     *
     * @param cacheMap 캐시에 저장할 키-값(캐시 키 - 동화 객체)
     * @param timeout  캐시 만료 시간
     * @param unit     만료 시간 단위
     */
    @Override
    public <T> void setMultiCaches(Map<String, T> cacheMap, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().multiSet(cacheMap);

        /**
         * MSET 명령은 만료 시간 설정 기능이 없습니다.
         * 저장된 각 캐시 키에 대해 개별적으로 expire 명령을 호출하여 만료 시간을 지정합니다.
         */
        for (String key : cacheMap.keySet()) {
            redisTemplate.expire(key, timeout, unit);
        }
        log.info("Cache Set: {} TTL: {} {}", cacheMap.keySet(), timeout, unit);

    }

    /**
     * 지정된 키에 해당하는 객체를 Redis 캐시에서 삭제합니다.
     *
     * @param key 캐시에 저장된 키
     */
    @Override
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 캐시 저장에 활용되는 Map을 생성합니다.
     *
     * @param dtoMap key: 객체 id, value: 객체
     * @param <T>    제네릭(메서드 호출된 순간 타입 결정)
     * @return Map - key: 캐시 key, value: 객체
     */
    @Override
    public <T> Map<String, T> createCacheMap(Map<Long, T> dtoMap, CachePrefix cachePrefix) {
        Map<String, T> cacheMap = dtoMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> cachePrefix.buildCacheKey(entry.getKey()), // key
                        Map.Entry::getValue // value
                ));

        return cacheMap;

    }

    /**
     * 캐시에 없는 Id를 반환합니다.
     *
     * @param ids          객체 Id 목록
     * @param cachedDtoMap 캐싱된 객체 Map
     * @return Id 리스트
     */
    @Override
    public <T> List<Long> findMissingIdList(List<Long> ids, Map<Long, T> cachedDtoMap) {
        return ids.stream()
                .filter(id -> !cachedDtoMap.containsKey(id))
                .toList();
    }

    /**
     * 캐시에 있던 객체와 DB에 있던 객체를 id 목록 순서대로 반환합니다.
     *
     * @param ids           객체 id 목록
     * @param cachedDtoMap  캐싱된 객체 Map
     * @param missingDtoMap 캐시에 없던 객체 Map
     * @return 객체 리스트
     */
    @Override
    public <T> List<T> mergedCachedAndMissing(List<Long> ids, Map<Long, T> cachedDtoMap, Map<Long, T> missingDtoMap) {
        return ids.stream()
                .map(id -> cachedDtoMap.getOrDefault(id, missingDtoMap.get(id)))
                .toList();
    }

}
