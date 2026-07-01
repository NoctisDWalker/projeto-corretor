package com.nerdev.auxcorretor.dto.conta;

import com.nerdev.auxcorretor.model.enums.PlanoEnum;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.model.enums.TipoContaEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContaCreateRequestDTO(
        @NotBlank
        String nome,
        @NotNull
        TipoContaEnum tipoConta,
        @NotBlank
        String documento,
        @NotNull
        PlanoEnum plano,
        @NotNull
        StatusContaEnum statusConta,
        @NotBlank
        String telefoneResponsavel,
        @NotBlank
        @Email
        String emailResponsavel
) {
}
