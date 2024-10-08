package com.tosi.tale.common.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponse {
    private HttpStatus status;
    private String code;
    private String message;

    @Builder
    public ErrorResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse of(ExceptionCode exceptionCode) {
        return ErrorResponse.builder()
                .code(exceptionCode.getCode())
                .status(exceptionCode.getStatus())
                .message(exceptionCode.getMessage())
                .build();
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .code("COMMMON")
                .status(status)
                .message(message)
                .build();
    }

}
