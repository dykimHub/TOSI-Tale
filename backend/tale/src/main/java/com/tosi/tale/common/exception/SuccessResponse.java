package com.tosi.tale.common.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
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



