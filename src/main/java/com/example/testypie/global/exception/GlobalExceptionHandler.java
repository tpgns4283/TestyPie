package com.example.testypie.global.exception;


import com.amazonaws.Response;
import io.jsonwebtoken.io.IOException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * Controller 내에서 발생하는 Exception에 대해 Catch해서 클라이언트에게 에러 응답을 보내주는 기능을 수행
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     * [Exception] API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
     *
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField()).append(":");
            sb.append(fieldError.getDefaultMessage());
            sb.append(", ");
        }

        final ErrorResponse res = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(sb));
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * [Exceptioin] API 호출 시 'Header' 내에 데이터 값이 존재하지 않는 경우
     *
     * @param e MissingRequestHeaderException
     * @return ResponseEntity
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingReqeustHeaderException(MissingRequestHeaderException e) {
        log.error("MissingRequestHeaderException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.NOT_VALID_HEADER_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 Body로 '객체' 데이터가 넘어오지 않았을 경우
     *
     * @param e HttpMessageNotReadbleException
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 클라이언트에서 request로 '파라미터' 데이터가 넘어오지 않았을 경우
     *
     * @param e MissingServletRequestParameterException
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException e) {
        log.error("handleMissingServletRequestParameterException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] 잘못된 서버 요청이 발생한 경우
     *
     * @param e HttpClientErrorException
     * @return  ResponseeEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpCliientErrorException.BadRequest", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * [Exception] 잘못된 주소로 요청한 경우
     *
     * @param e NoHandlerFoundException
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handlerNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("handlerNoHandlerFoundException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * [Exception] Null 값이 발생한 경우
     *
     * @param e NullPointerException
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * Input / Output 내에서 발생한 경우
     *
     * @param e IOException
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        log.error("handleIOException", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.IO_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * [Exception] 모든 Exception 경우 발생
     *
     * @param e Exception
     * @return  ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Exception", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HTTP_STATUS_OK);
    }

    /**
     * [Exception] CustomException에서 발생한 에러
     *
     * @param e CustomException
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("=================================================", e);
        log.error("Custom Exception", e);
        log.error("=================================================", e);
        final ErrorResponse res = ErrorResponse.of(ErrorCode.CUSTOM_EXCEPTION_ERROR, e.getMessage());
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    // ErrorCode를 보유한 예외 클래스 정의
    public static class CustomException extends RuntimeException {
        private final ErrorCode errorCode;

        @Builder
        public CustomException(String message, ErrorCode errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

        @Builder
        public CustomException(ErrorCode errorCode) {
            super(errorCode.getMessage());
            this.errorCode = errorCode;
        }
    }
}