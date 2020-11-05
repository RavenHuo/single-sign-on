package com.raven.sso.springboot.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-05 21:41
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = SsoException.class)
    public ResponseEntity<String> ssoExceptionHandler(SsoException exception) {
        return ResponseEntity.status(503).body(exception.getMsg());
    }
}
