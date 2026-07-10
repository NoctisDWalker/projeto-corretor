package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.model.Conta;
import com.nerdev.auxcorretor.model.enums.StatusContaEnum;
import com.nerdev.auxcorretor.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ContaValidator {

    private final ContaRepository contaRepository;

    public void validaCadastro(Conta conta) {
        camposUnicos(conta);
    }

    public void validarContaOperacional(Conta conta) {
        validaDataExpiracao(conta.getDataExpiracao());
        validaStatus(conta.getStatusConta());
        validaUnicoEmail(conta);
    }


    private void validaDataExpiracao(LocalDateTime dataExpiracao) {
        if (dataExpiracao.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Conta expirada");
        }
    }

    private void validaStatus(StatusContaEnum statusConta) {
        if (statusConta.acessoBloqueado()) {
            throw new BusinessException("Conta com acesso bloqueado");
        }
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

        validaUnicoEmail(conta);
    }

    private void validaUnicoEmail(Conta conta) {

        Optional<Conta> contaEncontrada =
                contaRepository.findByEmail(conta.getEmailResponsavel());

        if (contaEncontrada.isEmpty()) {
            return;
        }

        if (conta.getId() == null) {
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }

        if (!contaEncontrada.get().getId().equals(conta.getId())) {
            throw new DuplicateEntityException("E-mail já está em uso por outra conta.");
        }
    }

}
