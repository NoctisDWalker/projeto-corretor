package com.nerdev.auxcorretor.dto.corretor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

@Schema
public record CorretorCreateRequestDTO(

        @NotBlank(message = "Campo obrigatorio")
        String nome,

        @NotBlank(message = "Campo obrigatorio")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                message = "A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial"
        )
        String senha,

        @NotBlank(message = "Campo obrigatorio")
        @CPF
        String cpf,

        @NotBlank(message = "Campo obrigatorio")
        @Email
        String email,

        String telefone,

        @NotBlank(message = "Campo obrigatorio")
        String creci

) {
}
