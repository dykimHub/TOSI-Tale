package com.tosi.common.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApiClient {
    private final RestTemplate restTemplate;

    /**
     * 헤더를 포함한 HTTP GET 요청을 보내고, 응답을 단일 객체로 변환하여 반환합니다.
     *
     * @param url          요청할 API의 URL
     * @param headers      HTTP 헤더 정보
     * @param responseType 응답 데이터를 매핑할 클래스
     * @param <T>          제네릭(메서드 호출 시 type으로 결정됨)
     * @return API 응답을 responseType으로 변환하여 반환
     */
    public <T> T getObject(String url, HttpHeaders headers, Class<T> responseType) {
        log.info("Fetching:{}", url);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        ).getBody();

    }

    /**
     * HTTP GET 요청을 보내고, 응답을 단일 객체로 변환하여 반환합니다.
     *
     * @param url          요청할 API의 URL
     * @param responseType 응답 데이터를 매핑할 클래스
     * @param <T>          제네릭(메서드 호출 시 type으로 결정됨)
     * @return API 응답을 responseType으로 변환하여 반환
     */
    public <T> T getObject(String url, Class<T> responseType) {
        log.info("Fetching:{}", url);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        ).getBody();

    }

    /**
     * HTTP GET 요청을 보내고, 응답을 제네릭 리스트로 변환하여 반환합니다.
     *
     * @param url          요청할 API의 URL
     * @param responseType 응답 데이터를 매핑할 클래스
     * @param <T>          제네릭(메서드 호출 시 type으로 결정됨)
     * @return List<T> 형태로 변환된 API 응답
     */
    public <T> List<T> getObjectList(String url, Class<T> responseType) {
        log.info("Fetching:{}", url);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null, // GET 요청이라 body 없음
                new ParameterizedTypeReference<List<T>>() {
                } // 익명 클래스를 생성하면 런타임에도 타입 정보가 유지됨
        ).getBody();

    }

    /**
     * HTTP POST 요청을 보내고, 응답을 단일 객체로 변환하여 반환합니다.
     *
     * @param url 요청할 API의 URL
     * @param entity 요청 본문
     * @param responseType 응답 데이터를 매핑할 클래스
     * @param <T> 제네릭(메서드 호출 시 type으로 결정됨)
     * @return API 응답을 responseType으로 변환하여 반환
     */
    public <T> T postObject(String url, HttpEntity entity, Class<T> responseType) {
        log.info("Sending:{}", url);
        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                responseType
        ).getBody();

    }


}
