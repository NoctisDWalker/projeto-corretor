package com.nerdev.auxcorretor.model.enums;

import java.util.Set;

public enum StatusContaEnum {
    TRIAL(false),
    ATIVA(false),
    SUSPENSA(true),
    CANCELADA(true);

    private final boolean acessoBloqueado;
    private Set<StatusContaEnum> proximosStatus;

    StatusContaEnum(boolean acessoBloqueado) {
        this.acessoBloqueado = acessoBloqueado;
    }

    public boolean acessoBloqueado() {
        return acessoBloqueado;
    }

    static {
        TRIAL.proximosStatus = Set.of(ATIVA, SUSPENSA);
        ATIVA.proximosStatus = Set.of(SUSPENSA, CANCELADA);
        SUSPENSA.proximosStatus = Set.of(ATIVA, CANCELADA);
        CANCELADA.proximosStatus = Set.of(ATIVA);
    }

    public boolean podeTransicionarPara(StatusContaEnum destino) {
        return proximosStatus.contains(destino);
    }

}
