package com.nerdev.auxcorretor.exception;

import lombok.Getter;

public class RequiredFieldException extends BusinessException{

    @Getter
    private String campo;

    public RequiredFieldException(String message){
        super(message);
    }

    public RequiredFieldException(String message, String campo) {
        super(message);
        this.campo = campo;
    }
}
