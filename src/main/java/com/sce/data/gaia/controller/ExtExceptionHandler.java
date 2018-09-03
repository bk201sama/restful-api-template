package com.sce.data.gaia.controller;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExtExceptionHandler {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    protected ResponseEntity<String> handleHttpMessageNotReadable(HttpServletRequest request, Exception e) {
        return new ResponseEntity<>(Throwables.getStackTraceAsString(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        log.error("DefaultException Handler---Host: {} invokes url: {} ERROR: {}", request.getRemoteHost(), request.getRequestURL(), Throwables.getStackTraceAsString(e));
        return new ResponseEntity<>("sorry, 服务器走神了！", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
