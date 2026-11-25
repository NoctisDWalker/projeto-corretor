package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.dto.imobiliaria.ImobiliariaResponseDTO;
import com.nerdev.auxcorretor.dto.imobiliaria.endereco.EnderecoResponseDTO;

public record RegistrarImobiliariaResponseDTO(
        ContaResponseDTO conta,
        ImobiliariaResponseDTO imobiliaria,
        UsuarioCredencialResponseDTO credencialResponse
) {
}
