package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.exception.RequiredFieldException;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CorretorValidator {


    private final CorretorRepository corretorRepository;

    public void validarCadastro(Corretor corretor) {
        verificarCamposObrigatoriosCadastro(corretor);
        verificarIdNaoDeveVirPreenchido(corretor);
        verificarEmailDuplicado(corretor);
        verificarCpfUnico(corretor);
        verificarCreciUnico(corretor);
    }

    public void validarAtualizar(Corretor corretor) {
        verificarEmailDuplicado(corretor);
        verificarCreciUnico(corretor);
    }

    public void validarDeletar(UUID id) {
       // Todo: implementar validação com outras entidades
    }

    private void verificarCamposObrigatoriosCadastro(Corretor corretor) {
        if (isNullOrBlank(corretor.getEmail()) ||
        isNullOrBlank(corretor.getCpf()) || isNullOrBlank(corretor.getCreci())) {
            throw new RequiredFieldException("Campos obrigatórios não informados.");
        }
    }

    private void verificarEmailDuplicado(Corretor corretor) {
        Optional<Corretor> corretorEncontrado = corretorRepository.findByEmail(corretor.getEmail());
        validaUnico(corretorEncontrado, corretor);
    }

    private void verificarCreciUnico(Corretor corretor){
        Optional<Corretor> corretorEncontrado = corretorRepository.findByCreci(corretor.getCreci());
        validaUnico(corretorEncontrado, corretor);
    }
    private void verificarCpfUnico(Corretor corretor){
        Optional<Corretor> corretorEncontrado = corretorRepository.findByCpf(corretor.getCpf());
        validaUnico(corretorEncontrado, corretor);
    }


    private void verificarIdNaoDeveVirPreenchido(Corretor corretor) {
        if (corretor.getId() != null) {
            throw new BusinessException("ID não deve ser informado para cadastro.");
        }
    }

    private boolean isNullOrBlank(String campo) {
        return  (campo == null || campo.isBlank());
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
