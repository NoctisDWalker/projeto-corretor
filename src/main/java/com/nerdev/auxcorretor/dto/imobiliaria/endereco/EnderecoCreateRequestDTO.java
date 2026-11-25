package com.nerdev.auxcorretor.dto.imobiliaria.endereco;

import jakarta.validation.constraints.NotBlank;

public record EnderecoCreateRequestDTO(

        @NotBlank
        String logradouro,
        @NotBlank
        String numero,
        String complemento,
        @NotBlank
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String uf,
        String cep

) {
}
