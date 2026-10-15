package com.nerdev.auxcorretor.security;

import com.nerdev.auxcorretor.mapper.UsuarioAutenticadoMapper;
import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.repository.UsuarioRepository;
import com.nerdev.auxcorretor.validation.AuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenJwtConverter implements Converter<Jwt, CustomAuthenticationToken> {

    private final UsuarioRepository usuarioRepository;
    private final AuthValidator authValidator;
    private final UsuarioAutenticadoMapper usuarioAutenticadoMapper;

    @Override
    public CustomAuthenticationToken convert(Jwt jwtJson) {
        UUID subject = extrairId(jwtJson.getSubject());
        Usuario usuarioEncontrado = usuarioRepository.findById(subject)
                .orElseThrow(() -> {
                    log.warn("Falha na autenticação: Usuário não encontrado para o ID: {}", subject);
                    return new InvalidBearerTokenException("Usuário não encontrado.");
                });

        authValidator.validaLogin(usuarioEncontrado);
        UsuarioAutenticado usuarioAutenticado =
                usuarioAutenticadoMapper.toUsuarioAutenticado(usuarioEncontrado);

        return new CustomAuthenticationToken(usuarioAutenticado);
    }

    private UUID extrairId(String sub) {
        if (sub == null) {
            log.warn("Subject nulo");
            throw new InvalidBearerTokenException("Token Invalido");
        }
        try {
            return UUID.fromString(sub);
        } catch (IllegalArgumentException ex) {
            log.warn("Subject nao e um UUID: {}", sub);
            throw new InvalidBearerTokenException("Token Invalido", ex);
        }
    }

}
