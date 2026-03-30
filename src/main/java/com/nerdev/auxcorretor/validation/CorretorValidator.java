package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.exception.RequiredFieldException;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CorretorValidator {


    private final CorretorRepository corretorRepository;

    public void validarCadastro(Corretor corretor) {
        verificarCamposObrigatorios(corretor);
        verificarIdNaoDeveVirPreenchido(corretor);
        verificarEmailDuplicado(corretor);
    }

    public void validarAtualizar(Corretor corretor) {
        verificarCamposObrigatorios(corretor);
        verificarSeExistePorId(corretor.getId());
        verificarEmailDuplicado(corretor);
    }

    public void validarDeletar(UUID id) {
        verificarSeExistePorId(id);
    }

    private void verificarCamposObrigatorios(Corretor corretor) {
        if (isNullOrBlank(corretor.getSenha()) || isNullOrBlank(corretor.getEmail())) {
            throw new RequiredFieldException("Campos obrigatórios não informados.");
        }
    }

    private void verificarEmailDuplicado(Corretor corretor) {
        Optional<Corretor> emailExistente = corretorRepository.findByEmail(corretor.getEmail());
        if(corretor.getId() == null && emailExistente.isPresent()){
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }
        if (emailExistente.isPresent() && !emailExistente.get().getId().equals(corretor.getId())){
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }
    }

    private void verificarSeExistePorId(UUID id) {
        boolean idExistente = corretorRepository.existsById(id);
        if (id == null) {
            throw new BusinessException("ID deve ser informado.");
        }
        if(!idExistente){
            throw new BusinessException("ID não encontrado no banco.");
        }
    }

    private void verificarIdNaoDeveVirPreenchido(Corretor corretor) {
        if (corretor.getId() != null) {
            throw new BusinessException("ID não deve ser informado para cadastro.");
        }
    }

    private boolean isNullOrBlank(String campo) {
        return  (campo == null || campo.isBlank());
    }
}
