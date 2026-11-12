package com.nerdev.auxcorretor.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public enum StatusImovelEnum {

    DISPONIVEL(false),
    RESERVADO(false),
    VENDIDO(true),
    ALUGADO(true),
    INATIVO(true);

    private final boolean finalizado;

    private Set<StatusImovelEnum> proximosStatus;

    StatusImovelEnum(boolean finalizado) {
        this.finalizado = finalizado;
    }

    private static final List<StatusImovelEnum> STATUS_FINAIS = Arrays.stream(values())
            .filter(StatusImovelEnum::isFinalizado)
            .toList();

    public boolean isFinalizado()
    {
        return finalizado;
    }

    static {
        DISPONIVEL.proximosStatus = Set.of(RESERVADO, VENDIDO, ALUGADO, INATIVO);
        RESERVADO.proximosStatus = Set.of(DISPONIVEL, VENDIDO, ALUGADO);
        VENDIDO.proximosStatus = Set.of(DISPONIVEL, RESERVADO);
        ALUGADO.proximosStatus = Set.of(DISPONIVEL, RESERVADO);
        INATIVO.proximosStatus = Set.of(DISPONIVEL);
    }

    public boolean podeTransicionarPara (StatusImovelEnum destino) {
        return proximosStatus != null && proximosStatus.contains(destino);
    }

    public static List<StatusImovelEnum> statusFinais() {
        return STATUS_FINAIS;
    }

    public boolean isAtivo() {
        return !finalizado;
    }

}
