package com.tosi.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@Getter
public enum ParameterKey {
    TALE("taleIds");

    private final String key;

    /**
     * 쿼리 스트링을 반환합니다.
     *
     * @param baseUrl 호출할 API의 URL
     * @param ids 파라미터로 전달할 id 목록
     * @return key=id1&key=id2.. 형식의 쿼리 스트링
     */
    public String buildQueryString(String baseUrl, List<Long> ids) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(this.key, ids.toArray())
                .toUriString();
    }

}
