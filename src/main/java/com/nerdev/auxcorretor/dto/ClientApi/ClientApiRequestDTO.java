package com.nerdev.auxcorretor.dto.ClientApi;

public record ClientApiRequestDTO(
         String clientId,
         String clientSecret,
         String redirectUri,
         String scope
) {
}
