package com.nerdev.auxcorretor.model.enums;

import java.util.Optional;

public enum PlanoEnum {

    FREE_TRIAL(60),
    CORRETOR_INDIVIDUAL(null),
    IMOBILIARIA(null);

    private final Integer diasTrial;

    PlanoEnum(Integer diasTrial) {
        this.diasTrial = diasTrial;
    }

    public boolean isTrial() {
        return this == FREE_TRIAL;
    }

    public boolean permiteMultiplosAcessos() {
        return this == IMOBILIARIA;
    }

    public boolean permitePortalCliente() {
        return this != FREE_TRIAL;
    }

    public boolean permiteFinanceiro() {
        return this != FREE_TRIAL;
    }

    public boolean permitePosvenda() {
        return true; // liberado em todos os planos
    }

    public Optional<Integer> getDiasTrial() {
        return Optional.ofNullable(diasTrial);
    }

}
