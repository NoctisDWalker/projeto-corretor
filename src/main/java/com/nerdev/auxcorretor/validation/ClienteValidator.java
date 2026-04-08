package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.DuplicateEntityException;
import com.nerdev.auxcorretor.exception.RequiredFieldException;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
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
        verificarEmailDuplicado(cliente);
        verificarCpfDuplicado(cliente);
    }

    public void validaAtualizar(Cliente cliente) {
        verificarEmailDuplicado(cliente);
        verificarCpfDuplicado(cliente);
    }

    public void validaDeletar(Cliente cliente) {
        // Todo: Não permitir exclusão de clientes com vendas concretizadas
        verificarStatusAtual(cliente);
    }

    private void verificarEmailDuplicado(Cliente cliente) {
        Optional<Cliente> clienteEncontrado = clienteRepository
                .findByEmail(cliente.getEmail());
        validaUnico(clienteEncontrado, cliente);
    }
    private void verificarCpfDuplicado(Cliente cliente) {
        Optional<Cliente> clienteEncontrado = clienteRepository
                .findByCpf(cliente.getCpf());
        validaUnico(clienteEncontrado, cliente);
    }

    private void verificarCamposObrigatorios(Cliente cliente) {
        if (isNullOrBlank(cliente.getNome()) || isNullOrBlank(cliente.getTelefone())){
            throw new RequiredFieldException("Campos obrigatórios não informados.");
        }
    }

    private void verificarStatusAtual(Cliente cliente) {
        if (cliente.getStatusCliente() == StatusClienteEnum.INATIVO) {
            throw new BusinessException("Cliente já está inativo.");
        }
    }

    private boolean isNullOrBlank(String campo) {
        return (campo == null || campo.isBlank());
    }

    private void validaUnico(Optional<Cliente> clienteEncontrado, Cliente cliente){
        if (cliente.getId() == null && clienteEncontrado.isPresent()) {
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }
        if (clienteEncontrado.isPresent() && !clienteEncontrado.get().getId().equals(cliente.getId())) {
            throw new DuplicateEntityException("Dados informados já estão em uso.");
        }
    }

}
