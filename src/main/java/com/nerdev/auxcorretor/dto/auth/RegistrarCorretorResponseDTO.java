package com.nerdev.auxcorretor.dto.auth;

import java.util.UUID;

public record RegistrarCorretorResponseDTO(
        UUID usuarioId,
        UUID corretorId,
        String nomeExibicao,
        String login
) {
}
