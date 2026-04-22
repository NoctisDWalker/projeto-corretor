package com.nerdev.auxcorretor.model.enums;

import java.util.List;
import java.util.Set;

public enum StatusVisitaEnum {

    AGENDADA(false),
    CONFIRMADA(false),
    REMARCADA(false),
    REALIZADA(true),
    CANCELADA(true),
    NAO_COMPARECEU(true);

    private final boolean finalizado;
    private Set<StatusVisitaEnum> proximosStatus;

    StatusVisitaEnum(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public boolean isAtivo() {
        return !finalizado;
    }

    public static List<StatusVisitaEnum> statusFinais() {
        return List.of(REALIZADA, CANCELADA, NAO_COMPARECEU);
    }

    static {
        AGENDADA.proximosStatus = Set.of(CONFIRMADA, CANCELADA);

        CONFIRMADA.proximosStatus = Set.of(
                REMARCADA,
                REALIZADA,
                CANCELADA,
                NAO_COMPARECEU
                );

        REMARCADA.proximosStatus = Set.of(
                CONFIRMADA,
                REALIZADA,
                CANCELADA
        );

        CANCELADA.proximosStatus = Set.of(REMARCADA);
        REALIZADA.proximosStatus = Set.of();
        NAO_COMPARECEU.proximosStatus = Set.of();
    }

    public boolean podeTransicionarPara(StatusVisitaEnum destino) {
        return proximosStatus != null && proximosStatus.contains(destino);
    }
}
