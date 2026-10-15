package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.mapper.UsuarioAutenticadoMapper;
import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.CredencialUsuarioRepository;
import com.nerdev.auxcorretor.validation.AuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CredencialUsuarioRepository credencialUsuarioRepository;
    private final PasswordEncoder encoder;
    private final UsuarioAutenticadoMapper usuarioAutenticadoMapper;
    private final AuthValidator authValidator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        CredencialUsuario credencialUsuario = credencialUsuarioRepository.findByProviderWithUsuarioAndPerfis(ProviderTypeEnum.LOGIN_LOCAL, login)
                .orElseThrow(() -> new BadCredentialsException("Usuário e/ou senha incorretos!"));

        boolean matches = encoder.matches(password, credencialUsuario.getPasswordHash());

        if (matches) {
            authValidator.validaLogin(credencialUsuario.getUsuario());
            UsuarioAutenticado usuarioAutenticado = usuarioAutenticadoMapper.toUsuarioAutenticado(credencialUsuario.getUsuario());
            atualizaUltimoLogin(credencialUsuario);
            return new CustomAuthentication(usuarioAutenticado);
        }

        throw new BadCredentialsException("Usuário e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private void atualizaUltimoLogin(CredencialUsuario credencial) {
        try { // Trycat apenas funciona pois esta classe não possui @Transational
            credencialUsuarioRepository.atualizarUltimoLogin(credencial.getId(), LocalDateTime.now());
        } catch (DataAccessException ex) {
            log.error("Falha ao registrar o ultimo login. credencialId={}", credencial.getId(), ex);
        }
    }

}
