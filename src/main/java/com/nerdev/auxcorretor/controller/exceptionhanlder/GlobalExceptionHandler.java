package com.nerdev.auxcorretor.controller.exceptionhanlder;

import com.nerdev.auxcorretor.dto.exception.ErroResposta;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroResposta handleBusinessException(BusinessException ex) {
        return new ErroResposta(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage(), List.of());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta handleNotFoundException(NotFoundException ex) {
        return new ErroResposta(HttpStatus.NOT_FOUND.value(), ex.getMessage(), List.of());
    }

}
