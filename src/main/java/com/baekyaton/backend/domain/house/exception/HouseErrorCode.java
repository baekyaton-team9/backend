package com.baekyaton.backend.domain.house.exception;

import com.baekyaton.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum HouseErrorCode implements ErrorCode {
    IMAGE_NOT_FOUND("이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HOUSE_NOT_FOUND("빈집 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    HOUSE_DELETE_FAILED("빈집 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final HttpStatus httpStatus;
}