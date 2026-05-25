package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequestDTO(
        @NotNull ProviderTypeEnum providerType,
        @NotBlank String login,
        @NotBlank String senha
) {
}
