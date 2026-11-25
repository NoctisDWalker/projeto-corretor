package com.nerdev.auxcorretor.dto.imobiliaria;

import com.nerdev.auxcorretor.dto.imobiliaria.endereco.EnderecoResponseDTO;

import java.util.UUID;

public record ImobiliariaResponseDTO(

        UUID id,
        String nomeFantasia,
        String bio,
        String fotoUrl,
        EnderecoResponseDTO endereco

) {
}
