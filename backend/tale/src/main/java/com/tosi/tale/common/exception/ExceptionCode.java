package com.tosi.tale.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    ALL_TALES_NOT_FOUND(HttpStatus.NOT_FOUND, "TALES_001", "동화 리스트를 불러올 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
