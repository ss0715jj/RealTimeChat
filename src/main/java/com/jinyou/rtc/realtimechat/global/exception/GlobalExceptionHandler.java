package com.jinyou.rtc.realtimechat.global.exception;

import com.jinyou.rtc.realtimechat.global.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전체 애러를 핸들링 하기 위한 클래스
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비지니스 애러를 핸들링 한다
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse response = ErrorResponse.of(ex.getCode());
        return ResponseEntity.status(response.status()).body(response);
    }

    /**
     * 요청 시 잘못된 요청 데이터를 날린 오류를 핸들링 한다
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("[GlobalExceptionHandler] handleMethodArgumentNotValidException", ex);

        // 입력 필드 값에 대한 유효성 검사 결과 중 애러 메세지가 있다면
        // 해당 필드에 대한 애러 메세지를 담아서 내려준다
        // 그 외 라면 INVALID_REQUEST 애러 코드를 내려준다
        FieldError error = ex.getBindingResult().getFieldError();
        if (error != null && error.getDefaultMessage() != null) {
            String message = error.getDefaultMessage();
            ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, "BAD_REQUEST_BODY", message);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_REQUEST);
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 예기치 않은 오류를 핸들링 한다
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("[GlobalExceptionHandler] handleException", ex);

        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_ERROR);
        return ResponseEntity.status(response.status()).body(response);
    }
}
