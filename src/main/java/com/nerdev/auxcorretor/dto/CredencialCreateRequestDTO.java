package com.nerdev.auxcorretor.dto;

import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CredencialCreateRequestDTO(

        @NotNull
        ProviderTypeEnum providerType,
        @NotBlank
        String providerUserId,
        @NotNull
        String email,
        @NotBlank
        String senha

) {
}
