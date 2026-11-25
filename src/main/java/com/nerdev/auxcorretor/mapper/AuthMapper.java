package com.nerdev.auxcorretor.mapper;

import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorResponseDTO;
import com.nerdev.auxcorretor.dto.auth.RegistrarImobiliariaResponseDTO;
import com.nerdev.auxcorretor.dto.auth.UsuarioCredencialResponseDTO;
import com.nerdev.auxcorretor.dto.conta.ContaResponseDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.Imobiliaria;
import com.nerdev.auxcorretor.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final ImobiliariaMapper imobiliariaMapper;
    private final CorretorMapper corretorMapper;

    public RegistrarImobiliariaResponseDTO responseImobiliaria(
            ContaResponseDTO contaResponse,
            Imobiliaria imobiliaria,
            Usuario usuario
    ){

        CredencialUsuario credencialUsuario = usuario.getCredenciais().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("Credencial não encontrada"));

        UsuarioCredencialResponseDTO credUserResponse = new UsuarioCredencialResponseDTO(
                usuario.getId(),
                usuario.getNomeExibicao(),
                credencialUsuario.getEmail(),
                credencialUsuario.getProviderUserId()
        );

        return new RegistrarImobiliariaResponseDTO(
                contaResponse, imobiliariaMapper.toResponse(imobiliaria), credUserResponse
        );

    }

    public RegistrarCorretorResponseDTO responseCorretor(
            ContaResponseDTO contaResponse,
            Corretor corretor,
            Usuario usuario
    ){

        CredencialUsuario credencialUsuario = usuario.getCredenciais().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("Credencial não encontrada"));

        UsuarioCredencialResponseDTO credUserResponse = new UsuarioCredencialResponseDTO(
               usuario.getId(),
                usuario.getNomeExibicao(),
                credencialUsuario.getEmail(),
                credencialUsuario.getProviderUserId()
        );

        return new RegistrarCorretorResponseDTO(contaResponse, corretorMapper.toResponseDTO(corretor), credUserResponse);
    }

}
