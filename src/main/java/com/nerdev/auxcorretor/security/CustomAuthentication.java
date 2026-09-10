package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Usuario usuario;
    private final CredencialUsuario credencialUsuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Todo: Alterar posteriormente
    }

    @Override
    public String getName() {
        ProviderTypeEnum providerType = credencialUsuario.getProviderType();
        String providerUserId = credencialUsuario.getProviderUserId();
        return (providerType.name() + ":" + providerUserId);
    }
}
