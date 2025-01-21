package com.service.board.global.common;

import com.service.board.global.common.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseRes<String>> handleBaseException(BaseException e) {
        BaseResMsg baseResMsg = BaseResMsg.findByCode(e.getCode());
        return ResponseEntity.status(e.getCode()).body(new BaseRes<>(baseResMsg, e.getDetails()));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<BaseResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        List<String> errors = ex.getBindingResult()
//                .getAllErrors()
//                .stream()
//                .map(error -> {
//                    String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : "unknown";
//                    return String.format("%s: %s", fieldName, error.getDefaultMessage());
//                })
//                .collect(Collectors.toList());
//
//        BaseResponse<List<String>> baseResponse = new BaseResponse<>(
//                BaseResponseMessage.VALIDATION_ERROR,
//                errors
//        );
//
//        return ResponseEntity.badRequest().body(baseResponse);
//    }
//
//    @ExceptionHandler(MailException.class)
//    public ResponseEntity<BaseResponse<String>> handleMailException(MailException e){
//        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.EMAIL_SEND_FAIL, e.getMessage());
//        return ResponseEntity.badRequest().body(baseResponse);
//    }
//
//    @ExceptionHandler(IamportResponseException.class)
//    public ResponseEntity<BaseResponse<String>> handleIamportResponseException(IamportResponseException e){
//        BaseResponse<String> baseResponse = new BaseResponse<>(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage());
//        return ResponseEntity.badRequest().body(baseResponse);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<BaseResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse<>(BaseResponseMessage.ACCESS_DENIED));
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<BaseResponse<String>> handleAuthenticationException(AuthenticationException e) {
//        if (e instanceof BadCredentialsException) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseMessage.BAD_CREDENTIAL, e.getMessage()));
//        } else if (e instanceof InternalAuthenticationServiceException | e instanceof InsufficientAuthenticationException) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseMessage.ACCESS_DENIED, e.getMessage()));
//        }else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseMessage.INVALID_TOKEN, e.getMessage()));
//        }
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<BaseResponse<String>> handleJwtException(JwtException e) {
//        if(e instanceof ExpiredJwtException){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(BaseResponseMessage.JWT_TOKEN_EXPIRED, e.getMessage()));
//        } else if(e instanceof UnsupportedJwtException) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(BaseResponseMessage.JWT_TOKEN_UNSUPPORTED, e.getMessage()));
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(BaseResponseMessage.INTERNAL_SERVER_ERROR, e.getMessage()));
//        }
//    }
}
