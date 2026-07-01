package com.nerdev.auxcorretor.dto.conta;

import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.model.enums.TipoContaEnum;

import java.util.UUID;

public record ContaResponseDTO(
        UUID contaId,
        String nome,
        TipoContaEnum tipoConta,
        StatusContaEnum status
) {
}
