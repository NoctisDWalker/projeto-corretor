package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityContextUtil {

    private final UsuarioRepository usuarioRepository;

    public Usuario obterUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Operação não permitida. Usuario não autenticado");
        }

        if (authentication instanceof CustomAuthentication customAuthentication) {
            return customAuthentication.getUsuario();
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            String idStr = jwtAuth.getToken().getClaimAsString("user_id");

            if (idStr != null) {
                 return usuarioRepository.findById(UUID.fromString(idStr))
                         .orElseThrow(() -> new BusinessException("Úsuario não encontrado."));
            }
        }

        throw new BusinessException("Operação não permitida. Corretor não autenticado.");
    }
}
