package com.nerdev.auxcorretor.model.enums;

import java.util.List;
import java.util.Set;

public enum StatusContaEnum {
    ATIVA(false),
    SUSPENSA(true),
    CANCELADA(true);

    private final boolean finalizado;
    private Set<StatusContaEnum> proximosStatus;

    StatusContaEnum(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    static {
        ATIVA.proximosStatus = Set.of(SUSPENSA);
        SUSPENSA.proximosStatus = Set.of(ATIVA, CANCELADA);
        CANCELADA.proximosStatus = Set.of();
    }

    public List<StatusContaEnum> statusDeAcessoBloqueados() {
        return List.of(SUSPENSA, CANCELADA);
    }

    public boolean podeTransicionarPara(StatusContaEnum destino) {
        return proximosStatus.contains(destino);
    }

}
