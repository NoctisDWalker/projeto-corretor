package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CorretorValidator {

    private final CorretorRepository corretorRepository;

    public void validarAtualizar(Corretor corretor) {
        validarCorretorAtivo(corretor.isAtivo());
        verificarCreciUnico(corretor);
    }

    public void validarDeletar(Corretor corretor) {
        validarCorretorAtivo(corretor.isAtivo());
    }

    private void verificarCreciUnico(Corretor corretor){
        Optional<Corretor> corretorEncontrado = corretorRepository.findByCreci(corretor.getCreci());
        validaUnico(corretorEncontrado, corretor);
    }

    private void validarCorretorAtivo(Boolean ativo) {
        if (!ativo) {
            throw new BusinessException("Corretor está inativado");
        }
    }

    private void validaUnico(Optional<Corretor> corretorEncontrado, Corretor corretor){
        if(corretorEncontrado.isPresent() && corretor.getId() == null){
            throw new DuplicateEntityException("Dados informados já estão em uso");
        }
        if(corretorEncontrado.isPresent() && !corretorEncontrado.get().getId().equals(corretor.getId())){
            throw new DuplicateEntityException("Dados informados já estão em uso");
        }
    }

}
