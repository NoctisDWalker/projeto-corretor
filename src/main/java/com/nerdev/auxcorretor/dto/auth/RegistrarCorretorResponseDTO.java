package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorResponseDTO;

public record RegistrarCorretorResponseDTO(
        ContaResponseDTO conta,
        CorretorResponseDTO corretor,
        UsuarioCredencialResponseDTO credencialUsuario
) {
}
