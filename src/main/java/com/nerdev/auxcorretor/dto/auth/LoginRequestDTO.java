package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotBlank String login,
        @NotBlank String senha
) {
}
