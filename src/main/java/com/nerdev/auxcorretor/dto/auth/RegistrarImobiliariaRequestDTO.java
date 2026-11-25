package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.dto.conta.ContaCreateRequestDTO;
import com.nerdev.auxcorretor.dto.imobiliaria.ImobiliariaCreateRequestDto;
import com.nerdev.auxcorretor.dto.imobiliaria.endereco.EnderecoCreateRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegistrarImobiliariaRequestDTO(

        @Valid @NotNull(message = "Os dados da conta são obrigatórios.")
        ContaCreateRequestDTO conta,

        @Valid @NotNull(message = "Os dados da imobiliária são obrigatórios.")
        ImobiliariaCreateRequestDto imobiliaria,

        @Valid @NotNull(message = "Os dados de localização são obrigatórios.")
        EnderecoCreateRequestDTO endereco,

        @Valid @NotNull(message = "Os dados de acesso obrigatórios.")
        UsuarioCredencialRequestDTO credencialUsuario

) {
}
