package com.nerdev.auxcorretor.dto.corretor;

import jakarta.validation.constraints.NotBlank;

public record CorretorCreateRequestDTO(
        @NotBlank
        String nome,
        @NotBlank
        String bio,
        @NotBlank
        String cpf,
        @NotBlank
        String creci
) {
}
