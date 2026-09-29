package com.nerdev.auxcorretor.dto.ClientApi;

import java.util.UUID;

public record ClientApiResponseDTO(
         UUID id,
         String clientId,
         String redirectUri,
         String scope
) {
}
