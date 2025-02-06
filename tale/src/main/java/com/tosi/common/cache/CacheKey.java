package com.tosi.common.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CacheKey {
    TALE("taleCache::");

    private final String prefix;


    public String getKey(Long id) {
        return this.prefix + id;
    }
}
