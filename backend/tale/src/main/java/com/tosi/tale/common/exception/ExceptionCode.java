package com.tosi.tale.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    ALL_TALES_NOT_FOUND(HttpStatus.NOT_FOUND, "TALES_001", "동화 목록을 조회할 수 없습니다."),
    TALE_NOT_FOUND(HttpStatus.NOT_FOUND, "TALES_002", "해당 동화를 찾을 수 없습니다."),
    TALE_CONTENT_FETCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "TALES_004", "동화 본문을 불러올 수 없습니다."),
    TALE_CHARACTERS_FETCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "TALES_005", "동화 등장인물을 불러올 수 없습니다."),
    TALE_IMAGES_FETCH_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "TALES_006", "동화 삽화 목록을 불러올 수 없습니다."),
    PAGE_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "TALES_007", "동화 페이지 요청 객체가 비어있습니다."),

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_001", "유효하지 않은 토큰입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
