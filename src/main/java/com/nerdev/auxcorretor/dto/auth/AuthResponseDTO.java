package com.nerdev.auxcorretor.dto.auth;

import com.nerdev.auxcorretor.model.enums.PerfilUsuarioEnum;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;

import java.util.UUID;

public record AuthResponseDTO(

        UUID idUsuario,
        PerfilUsuarioEnum perfil,
        ProviderTypeEnum providerType,
        String nomeExibicao

) {

}
