package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.dto.auth.AuthResponseDTO;
import com.nerdev.auxcorretor.dto.auth.LoginRequestDTO;
import com.nerdev.auxcorretor.exception.AuthBussinessExeption;
import com.nerdev.auxcorretor.mapper.AuthMapper;
import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.repository.CredencialUsuarioRepository;
import com.nerdev.auxcorretor.validation.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final CredencialUsuarioRepository credUserRepository;
    private final AuthValidator authValidator;

    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        CredencialUsuario credencial = credUserRepository.findByProviderTypeAndProviderUserId
                (loginRequestDTO.providerType(), loginRequestDTO.login()).orElseThrow(
                () -> new AuthBussinessExeption("Usuário ou senha inválidos"));

        boolean matchesPassword = encoder.matches(loginRequestDTO.senha(), credencial.getPasswordHash());

        if (!matchesPassword) {
            throw new AuthBussinessExeption("Usuário ou senha inválidos");
        }

        authValidator.validaLogin(credencial);
        CredencialUsuario credencialAtualizada = atualizaUltimoLogin(credencial);

        return AuthMapper.fromAuth(credencialAtualizada);
    }

    private CredencialUsuario atualizaUltimoLogin(CredencialUsuario credencial) {
        credencial.setLastLoginAt(LocalDateTime.now());
       return credUserRepository.save(credencial);
    }

}
