package com.board.api.global.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseExc.class)
    public ResponseEntity<BaseRes<String>> handleBaseException(BaseExc e){
        return ResponseEntity.badRequest().body(new BaseRes<>(Objects.requireNonNull(BaseMsg.findByCode(e.getCode()))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseRes<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : "unknown";
                    return String.format("%s: %s", fieldName, error.getDefaultMessage());
                })
                .collect(Collectors.toList());
        BaseRes<List<String>> baseRes = new BaseRes<>( BaseMsg.VALIDATION_ERROR, errors );

        return ResponseEntity.badRequest().body(baseRes);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public Object handleServletRequestBindingException(ServletRequestBindingException e, 
                                                     HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     RedirectAttributes redirectAttributes) {
        // AJAX 요청인 경우 JSON 응답
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            return ResponseEntity.badRequest().body(new BaseRes<>(BaseMsg.LOGIN_REQUIRED));
        }
        
        // 일반 요청인 경우 리다이렉트
        redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
        return new ModelAndView("redirect:/");
    }
}
