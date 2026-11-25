package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegistrarCorretorRequestDTO(

        @Valid @NotNull(message = "Os dados da conta são obrigatórios.")
        ContaCreateRequestDTO conta,

        @Valid @NotNull(message = "Os dados do corretor são obrigatórios.")
        CorretorCreateRequestDTO corretor,

        @Valid @NotNull(message = "Os dados de acesso obrigatórios.")
        UsuarioCredencialRequestDTO credencialUsuario

        ) {
}
