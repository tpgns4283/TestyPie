package com.example.testypie.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.swing.*;

@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {
    /**
     * ***** Global Error CodeList *****
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 409 : Conflict
     * 422 : Unprocessable Entity
     * 500 : INTERNAL SERVER
     */

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT_VALUE(400,  "입력값이 올바르지 않습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHENTICATED_USERS(401, "인증이 필요합니다."),

    /* 403 FORBIDDEN : 접근권한 없음 */
    ACCESS_DENIED(403, "접근이 거부되었습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    USER_NOT_FOUND(404, "해당 유저 정보를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(404, "해당 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT : 데이터 중복 */
    DUPLICATE_RESOURCE(409, "데이터가 이미 존재합니다"),

    /* 422 Unprocessable Entity */
    IMAGE_NOT_READABLE(422, "파일을 읽을 수 없습니다."),

    /* 500 INTERNAL_SERVER_ERROR */
    SERVER_ERROR(500,  "예기치 못한 오류가 발생하였습니다.");

    private final int status;

    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
