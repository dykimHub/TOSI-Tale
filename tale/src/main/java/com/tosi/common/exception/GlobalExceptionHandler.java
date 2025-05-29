package com.tosi.common.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ce) {
        ErrorResponse errorResponse = ErrorResponse.of(ce.getExceptionCode());
        //log.info(ce.getMessage());
        return ResponseEntity.status(errorResponse.getStatus())
                .body(errorResponse);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        //log.info(e.getMessage());
        return ResponseEntity.status(errorResponse.getStatus())
                .body(errorResponse);

    }


}
