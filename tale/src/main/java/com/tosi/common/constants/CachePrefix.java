package com.tosi.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public enum CachePrefix {
    // 동화 서비스
    TALE("taleCache::"),
    TALE_DETAIL("taleDetailCache::"),

    // 회원 서비스
    POPULAR_TALE("popularTaleIdCache::"),
    FAVORITE_TALE("favoriteTaleIdCache::"),

    // 커스텀 동화 서비스
    CUSTOM_TALE("customTaleCache::"),
    CUSTOM_TALE_DETAIL("customTaleDetailCache::");


    private final String prefix;


    public String buildCacheKey(Long id) {
        return this.prefix + id;
    }

    public List<String> buildCacheKeys(List<Long> ids) {
        return ids.stream()
                .map(this::buildCacheKey)
                .toList();
    }
}
