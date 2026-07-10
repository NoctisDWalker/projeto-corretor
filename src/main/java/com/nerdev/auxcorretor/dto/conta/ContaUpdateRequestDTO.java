package com.nerdev.auxcorretor.dto.conta;

import jakarta.validation.constraints.Email;

public record ContaUpdateRequestDTO(

        String nome,
        @Email
        String emailResponsavel,
        String telefoneResponsavel

) {
}
