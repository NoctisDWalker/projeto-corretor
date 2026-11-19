package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.model.Conta;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Usuario;
import com.nerdev.auxcorretor.repository.ContaRepository;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.UsuarioRepository;
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
    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public Corretor obterCorretorLogado() {
        UsuarioAutenticado usuarioAutenticado = securityContextUtil.obterUsuarioLogado();
        return corretorRepository.findByUsuarioId(usuarioAutenticado.id())
                .orElseThrow(() -> new AccessDeniedException("Corretor não encontrado."));
    }

    public Usuario obterUsuarioLogado(){
        UsuarioAutenticado usuarioAutenticado = securityContextUtil.obterUsuarioLogado();
        return usuarioRepository.findById(usuarioAutenticado.id())
                .orElseThrow(() -> new AccessDeniedException("Usuário não encontrado."));
    }

    public Conta obterContaLogado(){
        UsuarioAutenticado usuarioAutenticado = securityContextUtil.obterUsuarioLogado();
        return contaRepository.findById(usuarioAutenticado.contaId())
                .orElseThrow(() -> new AccessDeniedException("Conta não encontrada."));
    }

}
