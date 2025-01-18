package com.baekyaton.backend.global.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    String getMessage();
    HttpStatus getHttpStatus();
}
