package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Conta;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContaValidator {

    private final ContaRepository contaRepository;

    public void validaCadastro(Conta conta) {
        camposUnicos(conta);
    }


    private void camposUnicos(Conta conta) {
        boolean existeNome = contaRepository.existsByNomeIgnoreCase(conta.getNome());
        boolean existeDocumento = contaRepository.existsByDocumentoIgnoreCase(conta.getDocumento());

        if (existeNome) {
            throw new BusinessException("Ja existe uma conta com o nome informado.");
        }
        if (existeDocumento) {
            throw new BusinessException("Ja existe uma conta com o documento informado.");
        }

    }

}
