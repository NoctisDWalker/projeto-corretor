package com.nerdev.auxcorretor.security;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextUtil {

    public UsuarioAutenticado obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.getPrincipal() instanceof UsuarioAutenticado usuario) {
            return usuario;
        }
        throw new InsufficientAuthenticationException("Nenhum usuário autenticado.");
    }

}
