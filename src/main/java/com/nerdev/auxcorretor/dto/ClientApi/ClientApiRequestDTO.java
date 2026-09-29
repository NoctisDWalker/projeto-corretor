package com.nerdev.auxcorretor.dto.ClientApi;

import java.util.UUID;

public record ClientApiRequestDTO(
         String clientId,
         String clientSecret,
         String redirectUri,
         String scope
) {
}
