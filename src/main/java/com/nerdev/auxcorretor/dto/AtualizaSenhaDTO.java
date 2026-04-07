package com.nerdev.auxcorretor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema
public record AtualizaSenhaDTO(
        @NotBlank(message = "Campo obrigatorio")
        String senhaAtual,

        @NotBlank(message = "Campo obrigatorio")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
        )
        String novaSenha,

        @NotBlank(message = "Campo obrigatorio")
        String confirmacaoSenha
) {
}
