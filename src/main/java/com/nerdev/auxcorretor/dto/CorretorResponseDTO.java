package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema
public record CorretorResponseDTO(

        UUID uuid,
        String nome,
        String email,
        String telefone,
        String creci
) {
}
