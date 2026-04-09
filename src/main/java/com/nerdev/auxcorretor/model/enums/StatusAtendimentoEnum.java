package com.nerdev.auxcorretor.model.enums;

import java.util.List;

public enum StatusAtendimentoEnum {

    NOVO_LEAD(false),
    PRIMEIRO_CONTATO(false),
    EM_ATENDIMENTO(false),
    VISITA_AGENDADA(false),
    VISITA_REALIZADA(false),
    PROPOSTA_ENVIADA(false),
    NEGOCIANDO(false),

    VENDA_CONCLUIDA(true),
    PERDIDO(true),
    INATIVO(true);

    private final boolean finalizado;

    StatusAtendimentoEnum(boolean finalizado){
        this.finalizado = finalizado;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public boolean isAtivo() {
        return !finalizado;
    }

    public static List<StatusAtendimentoEnum> statusFinais(){
        return List.of(VENDA_CONCLUIDA, PERDIDO, INATIVO);
    }

}
