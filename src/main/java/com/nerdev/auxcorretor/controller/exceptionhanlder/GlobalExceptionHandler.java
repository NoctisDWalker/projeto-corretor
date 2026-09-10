package com.nerdev.auxcorretor.controller.exceptionhanlder;

import com.nerdev.auxcorretor.dto.exception.ErroResposta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErroResposta handleBadCredentialsException(BadCredentialsException ex) {
        return new ErroResposta(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), List.of());
    }


}
