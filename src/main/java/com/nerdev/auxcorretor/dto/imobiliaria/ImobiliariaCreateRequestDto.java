package com.nerdev.auxcorretor.dto.imobiliaria;

import jakarta.validation.constraints.NotBlank;

public record ImobiliariaCreateRequestDto(

        @NotBlank
        String nomeFantasia,
        String bio,
        String fotoUrl
) {
}
