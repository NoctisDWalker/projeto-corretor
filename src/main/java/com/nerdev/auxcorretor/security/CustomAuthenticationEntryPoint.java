package com.nerdev.auxcorretor.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nerdev.auxcorretor.dto.exception.ErroResposta;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErroResposta erro = new ErroResposta(
                HttpStatus.UNAUTHORIZED.value(),
                "Acesso negado. Você precisa estar autenticado para acessar este recurso.",
                List.of()
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        new ObjectMapper().writeValue(response.getOutputStream(), erro);
    }
}
