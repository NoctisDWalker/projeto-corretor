package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.exception.RequiredFieldException;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ClienteValidator {

    private ClienteRepository clienteRepository;

    public void validaSalvar(Cliente cliente) {
        verificarCamposObrigatorios(cliente);
        verificarStatusInicial(cliente);
        verificarEmailDuplicado(cliente);
    }

    public void validaAtualizar(Cliente cliente) {
        verificarCamposObrigatorios(cliente);
        verificarStatusAtual(cliente);
        verificarEmailDuplicado(cliente);
    }

    public void validaDeletar(Cliente cliente) {
        verificarStatusAtual(cliente);
    }


    private void verificarEmailDuplicado(Cliente cliente) {
        Optional<Cliente> clienteEncontrado = clienteRepository
                .findByEmailAndCorretorId(cliente.getEmail(), cliente.getCorretor().getId());

        if (cliente.getId() == null && clienteEncontrado.isPresent()) {
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }
        if (clienteEncontrado.isPresent() && !clienteEncontrado.get().getId().equals(cliente.getId())) {
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }

    }

    private void verificarCamposObrigatorios(Cliente cliente) {
        if (isNullOrBlank(cliente.getNome()) || isNullOrBlank(cliente.getEmail()) ||
                isNullOrBlank(cliente.getTelefone()) || cliente.getStatusCliente() == null || cliente.getCorretor() == null){
            throw new RequiredFieldException("Campos obrigatórios não informados.");
        }
    }

    private void verificarStatusInicial(Cliente cliente) {
        if (cliente.getStatusCliente() == StatusClienteEnum.PERDIDO) {
            throw new BusinessException("Status do cliente invalido para criação");
        }
    }
    private void verificarStatusAtual(Cliente cliente) {
        if (cliente.getStatusCliente() == StatusClienteEnum.PERDIDO) {
            throw new BusinessException("Status do cliente invalido para alteração ou deleção");
        }
    }

    private boolean isNullOrBlank(String campo) {
        return (campo == null || campo.isBlank());
    }

}
