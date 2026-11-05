package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.security.SecurityContextUtil;
import com.nerdev.auxcorretor.security.UsuarioAutenticado;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceUtils {

    private final SecurityContextUtil securityContextUtil;
    private final CorretorRepository corretorRepository;

    public Corretor obterCorretorLogado() {
        UsuarioAutenticado usuarioAutenticado = securityContextUtil.obterUsuarioLogado();
        return corretorRepository.findByUsuarioId(usuarioAutenticado.id())
                .orElseThrow(() -> new AccessDeniedException("Corretor não encontrado."));
    }

}
