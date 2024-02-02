package com.example.testypie.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private int statusCode;             // 에러 상태 코드
    private String errorMessage;        // 에러 구분 코드
    private List<FieldError> errors;    // 상세 에러 메시지
    private String reason;              // 에러 이유

    /**
     * ErrorResponse 생성자 1
     *
     * @param code ErrorCode
     */
    @Builder
    public ErrorResponse(final ErrorCode code) {
        this.statusCode = code.getStatus();
        this.errorMessage = code.getMessage();
        this.errors = new ArrayList<>();
    }

    /**
     * ErrorResponse 생성자 2
     *
     * @param code   ErrorCode
     * @param reason String
     */
    @Builder
    public ErrorResponse(final ErrorCode code, final String reason) {
        this.statusCode = code.getStatus();
        this.errorMessage = code.getMessage();
        this.reason = reason;
    }

    /**
     * ErrorResponse 생성자 -3
     *
     * @param code  ErrorCode
     * @param errors List<FieldError>
     */
    @Builder
    protected ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.statusCode = code.getStatus();
        this.errorMessage = code.getMessage();
        this.errors = errors;
    }

    /**
     * Global Exception 전송 타입 -1
     *
     * @param code          ErrorCode
     * @param bindingResult BindingResult
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    /**
     * Global Exception 전송 타입 -2
     *
     * @param code  ErrorCode
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    /**
     * Global Exception 전송 타입 -3
     *
     * @param code      ErrorCode
     * @param reason    String
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }

}
