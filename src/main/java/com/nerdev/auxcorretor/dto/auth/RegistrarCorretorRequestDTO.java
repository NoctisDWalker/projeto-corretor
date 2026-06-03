package com.nerdev.auxcorretor.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegistrarCorretorRequestDTO(

        // Usuario
        @NotBlank
        String nomeExibicao,

        // Corretor
        @NotBlank
        String nome,
        @NotBlank
        String bio,
        @NotBlank
        String cpf,
        @NotBlank
        String creci,

        // Conta
        @NotNull
        UUID contaId,

        // CredencialUsuario
        @NotBlank
        String email,
        @NotBlank
        String login,
        @NotBlank
        String senha

        ) {
}
