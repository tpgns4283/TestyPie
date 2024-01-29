package com.example.testypie.global.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String errorMessage;

    /**
     * ErrorResponse 생성자 1
     *
     * @param code ErrorCode
     */
    @Builder
    public ErrorResponse(final ErrorCode code) {
        this.statusCode = code.getStatus();
        this.errorMessage = code.getMessage();
    }

    @Builder
    public ErrorResponse(final int statusCode, final String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
