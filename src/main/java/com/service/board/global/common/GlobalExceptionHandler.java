package com.service.board.global.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<String>> handleBaseException(BaseException e){
        return ResponseEntity.badRequest().body(new BaseResponse<>(Objects.requireNonNull(BaseMsg.findByCode(e.getCode()))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : "unknown";
                    return String.format("%s: %s", fieldName, error.getDefaultMessage());
                })
                .collect(Collectors.toList());
        BaseResponse<List<String>> baseResponse = new BaseResponse<>( BaseMsg.VALIDATION_ERROR, errors );

        return ResponseEntity.badRequest().body(baseResponse);
    }
}
