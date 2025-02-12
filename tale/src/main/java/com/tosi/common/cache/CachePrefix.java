package com.tosi.common.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CachePrefix {
    TALE("taleCache::"),
    POPULAR_TALE("popularTaleCache::"),
    FAVORITE_TALE("favoriteTaleCache::");

    private final String prefix;


    public String buildCacheKey(Long id) {
        return this.prefix + id;
    }
}
