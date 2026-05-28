package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.auth.AuthResponseDTO;
import com.nerdev.auxcorretor.exception.AuthBussinessExeption;
import com.nerdev.auxcorretor.model.CredencialUsuario;

public class AuthMapper {

    public static AuthResponseDTO fromAuth(CredencialUsuario credencialUsuario) {

        if (credencialUsuario == null) {
            throw new AuthBussinessExeption("CredencialUsuario não pode ser nula");
        }

        return new AuthResponseDTO(
                credencialUsuario.getUsuario().getId(),
                credencialUsuario.getUsuario().getPerfilUsuario(),
                credencialUsuario.getProviderType(),
                credencialUsuario.getUsuario().getNomeExibicao()
        );
    }

}
