package com.nerdev.auxcorretor.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record UsuarioCredencialRequestDTO(
        // Usuario
        @NotBlank
        String nomeExibicao,

        // CredencialUsuario
        @NotBlank
        String email,
        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}
