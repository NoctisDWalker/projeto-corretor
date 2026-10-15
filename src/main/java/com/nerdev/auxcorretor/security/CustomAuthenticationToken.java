package com.nerdev.auxcorretor.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Objects;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final UsuarioAutenticado usuarioAutenticado;

    public CustomAuthenticationToken(UsuarioAutenticado usuarioAutenticado) {
        super(converterAuthorities(usuarioAutenticado));
        this.usuarioAutenticado = usuarioAutenticado;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UsuarioAutenticado getPrincipal() {
        return usuarioAutenticado;
    }

    @Override
    public String getName() {
        return this.usuarioAutenticado.id().toString();
    }

    private static List<? extends GrantedAuthority> converterAuthorities(UsuarioAutenticado usuarioAutenticado) {
        Objects.requireNonNull(usuarioAutenticado, "usuarioAutenticado nao pode ser nulo.");
        return usuarioAutenticado.perfilUsuario().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

}
