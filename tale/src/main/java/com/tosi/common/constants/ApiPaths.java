package com.tosi.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@Getter
public enum ApiPaths {
    // 동화 서비스
    MULTI_TALE("/bulk", "taleIds"),
    TALE_DETAIL("/content/", null),
    MULTI_TALE_DETAIL("/content/bulk", "taleIds"),

    // 회원 서비스
    AUTH("/auth", null);


    private final String path;
    private final String param;

    public String buildPath(String baseURL) {
        return baseURL + this.path;
    }

    public String buildPath(String baseURL, Long id) {
        return baseURL + this.path + id;
    }

    public String buildPath(String baseURL, List<Long> ids) {
        return UriComponentsBuilder.fromHttpUrl(baseURL + this.path)
                .queryParam(this.param, ids.toArray())
                .toUriString();

    }

}
