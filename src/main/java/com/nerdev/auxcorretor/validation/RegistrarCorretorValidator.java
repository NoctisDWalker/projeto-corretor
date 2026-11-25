package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.dto.auth.RegistrarCorretorRequestDTO;
import com.nerdev.auxcorretor.dto.corretor.CorretorCreateRequestDTO;
import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Conta;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import com.nerdev.auxcorretor.repository.ContaRepository;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.CredencialUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrarCorretorValidator {

    private final ContaRepository contaRepository;
    private final CorretorRepository corretorRepository;
    private final CredencialUsuarioRepository credencialUsuarioRepository;

    public void validarCadastro(Corretor corretor, CredencialUsuario credencialUsuario, Conta conta) {
        validarLogin(credencialUsuario.getProviderUserId(), credencialUsuario.getProviderType());
        validarConta(conta.getId());
        validarEmail(credencialUsuario.getEmail());
        validarCpf(corretor.getCpf());
        validarCreci(corretor.getCreci());
    }

    private void validarLogin(String login, ProviderTypeEnum providerTypeEnum) {
        boolean existeLogin = credencialUsuarioRepository.existsByProviderUserIdAndProviderType(login, providerTypeEnum);
        if (existeLogin) {
            throw new BusinessException("Login já existente.");
        }
    }

    private void validarConta(UUID uuid) {
        Conta conta = contaRepository.findById(uuid)
                .orElseThrow(() -> new BusinessException("Conta inexistente"));

        if (conta.getStatusConta().acessoBloqueado()) {
            throw new BusinessException("Conta cancelada.");
        }
    }

    private void validarEmail(String email) {
        boolean existeEmail = credencialUsuarioRepository.existsByEmail(email);
        if (existeEmail) {
            throw new BusinessException("Email informado já está em uso");
        }
    }

    private void validarCpf(String cpf) {
        boolean existeCpf = corretorRepository.existsByCpf(cpf);
        if (existeCpf) {
            throw new BusinessException("CPF informado já está em uso");
        }
    }

    private void validarCreci(String creci) {
        boolean existeCreci = corretorRepository.existsByCreci(creci);
        if (existeCreci) {
            throw new BusinessException("Creci informado já está em uso");
        }
    }

}
