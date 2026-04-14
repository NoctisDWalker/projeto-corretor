package com.nerdev.auxcorretor.model.enums;

import java.util.List;

public enum StatusVisitaEnum {

    AGENDADA(false),
    CONFIRMADA(false),
    REMARCADA(false),
    REALIZADA(true),
    CANCELADA(true),
    NAO_COMPARECEU(true);

    private final boolean finalizado;

    StatusVisitaEnum(boolean finalizado){
        this.finalizado = finalizado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public boolean isAtivo() {
        return !finalizado;
    }

    public static List<StatusVisitaEnum> statusFinais(){
        return List.of(REALIZADA, CANCELADA, NAO_COMPARECEU);
    }

}
