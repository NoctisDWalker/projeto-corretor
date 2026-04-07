package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema
public record CorretorResponseDTO(

        UUID id,
        String nome,
        String cpf,
        String email,
        String telefone,
        String creci,
        Boolean ativo,
        LocalDate dataCadastro
) {
}
