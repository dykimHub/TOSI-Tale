package com.tosi.common.service;

import com.tosi.common.constants.CachePrefix;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public interface CacheService {
    <T> Optional<T> getCache(String key, Class<T> type);

    <T> List<T> getMultiCaches(List<String> keys, Class<T> type);

    <T> void setCache(String key, T value, long timeout, TimeUnit unit);

    <T> void setMultiCaches(Map<String, T> cacheMap, long timeout, TimeUnit unit);

    void deleteCache(String key);

    <T> Map<String, T> createCacheMap(Map<Long, T> dtoMap, CachePrefix cachePrefix);
}
