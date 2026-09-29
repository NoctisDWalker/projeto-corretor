package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.CredencialUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final CredencialUsuarioRepository credencialUsuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] partes = username.split(":");
        String providerTypeString = partes[0];
        String providerUserId = partes[1];
        ProviderTypeEnum providerType = ProviderTypeEnum.valueOf(providerTypeString);

        CredencialUsuario credencial = credencialUsuarioRepository
                .findByProviderTypeAndProviderUserId(providerType, providerUserId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        List<SimpleGrantedAuthority> authorities = credencial.getUsuario().getPerfilUsuario().stream()
                .map(perfil -> new SimpleGrantedAuthority(perfil.name())).toList();

        return User.withUsername(username)
                .password(credencial.getPasswordHash())
                .authorities(authorities)
                .build();
    }
}
