package com.tosi.common.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SuccessResponse {

    private HttpStatus status;
    private String code;
    private String message;

    @Builder
    public SuccessResponse(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static SuccessResponse of(String message) {
        return SuccessResponse.builder()
                .status(HttpStatus.OK)
                .code("SUCCESS")
                .message(message)
                .build();
    }

}



