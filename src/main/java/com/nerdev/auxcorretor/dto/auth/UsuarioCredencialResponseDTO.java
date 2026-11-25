package com.nerdev.auxcorretor.dto.auth;

import java.util.UUID;

public record UsuarioCredencialResponseDTO(
        // Usuario
        UUID id,
        String nomeExibicao,

        // CredencialUsuario
        String email,
        String login
) {
}
