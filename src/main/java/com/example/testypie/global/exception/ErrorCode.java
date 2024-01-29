package com.example.testypie.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {
    /* Bugreport */
    SELECT_BUGREPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 버그 리포트는 존재하지 않습니다."),
    /* Category */
    SELECT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리는 존재하지 않습니다."),
    SELECT_CATEGORY_NOT_MATCH(HttpStatus.NOT_FOUND, "존재하지 않는 상위 카테고리 입니다."),
    /* Comment(끝) */
    SELECT_COMMENT_NOT_EXIST(HttpStatus.NOT_FOUND, "해당 댓글은 존재하지 않습니다."),
    SELECT_COMMENT_INVALID_USER(HttpStatus.FORBIDDEN, "해당 댓글의 작성자만 접근할 수 있습니다."),
    SELECT_COMMENT_NOT_MATCH_ORIGIN(HttpStatus.CONFLICT, "요청한 댓글의 위치가 존재하지 않습니다."),
    /* USER */
    SIGNUP_DUPLICATED_USER_ACCOUNT(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    SIGNUP_DUPLICATED_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    SIGNUP_DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    LOGIN_INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "올바르지 않은 아이디로 로그인을 시도하셨습니다."),
    LOGIN_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "올바르지 않은 비밀번호로 로그인을 시도하셨습니다."),
    UPDATE_IDENTICAL_EMAIL(HttpStatus.CONFLICT, "이전과 동일한 이메일을 입력하셨습니다."),
    SELECT_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 회원은 존재하지 않습니다."),
    DELETE_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "삭제하시려는 회원은 존재하지 않습니다."),
    UPDATE_USER_INVALID_AUTHORIZATION(HttpStatus.FORBIDDEN, "해당 회원의 수정 권한이 없습니다."),
    DELETE_USER_INVALID_AUTHORIZATION(HttpStatus.FORBIDDEN, "해당 회원의 삭제 권한이 없습니다."),
    LOGOUT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    LOGOUT_REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    /* CommentLike */
    /* Feedback(끝) */
    SELECT_FEEDBACK_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 피드백이 존재하지 않습니다."),
    /* Product(끝) */
    SELECT_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 product는 존재하지 않습니다."),
    SELECT_PRODUCT_INVALID_AUTHORIZATION(HttpStatus.FORBIDDEN, "해당 product의 접근 권한이 없습니다."),
    SELECT_PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 product의 카테고리가 일치하지 않습니다."),
    /* ProductLike */
    /* Reward */
    /* Survey(끝) */
    SELECT_SURVEY_NOT_FOUND(HttpStatus.NOT_FOUND, "찾으시는 설문지가 존재하지 않습니다."),
    SELECT_SURVEY_INVALID_AUTHORIZATION(HttpStatus.FORBIDDEN, "설문지에 대한 접근권한이 없습니다."),
    /* User */
    /* Util(S3Uploader) */
    SELECT_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지를 찾을 수 없습니다."),
    CREATE_IMAGE_FAIL(HttpStatus.NOT_FOUND, "해당 이미지 업로드에 실패했습니다."),
    SELECT_IMAGE_NOT_READABLE(HttpStatus.UNPROCESSABLE_ENTITY, "해당 이미지를 불러올 수 없습니다.");

    private final HttpStatus code;

    private final String message;

    ErrorCode(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

}
