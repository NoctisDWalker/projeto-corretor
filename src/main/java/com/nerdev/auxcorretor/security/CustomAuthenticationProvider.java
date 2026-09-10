package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.CredencialUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CredencialUsuarioRepository credencialUsuarioRepository;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        CredencialUsuario credencialUsuario = credencialUsuarioRepository.findByProviderTypeAndProviderUserId(ProviderTypeEnum.LOGIN_LOCAL, login)
                .orElseThrow(() -> new BadCredentialsException("Usuário e/ou senha incorretos!"));

        boolean matches = encoder.matches(password, credencialUsuario.getPasswordHash());

        if (matches) {
            return new CustomAuthentication(credencialUsuario.getUsuario(), credencialUsuario);
        }

        throw new BadCredentialsException("Usuário e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
