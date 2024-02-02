package com.example.testypie.global.exception;

import lombok.Getter;

@Getter
public enum SuccessCode {
    // 성공
    SUCCESS(200, "정상 처리되었습니다.");

    // 성공 코드의 '코드 상태'를 반환한다.
    private final int status;

    private final String message;

    // 생성자 구성
    SuccessCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }



}
