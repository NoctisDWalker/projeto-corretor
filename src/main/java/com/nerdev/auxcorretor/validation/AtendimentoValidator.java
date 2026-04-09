package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.exception.RequiredFieldException;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import com.nerdev.auxcorretor.repository.AtendimentoRepository;
import com.nerdev.auxcorretor.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtendimentoValidator {

    private final AtendimentoRepository atendimentoRepository;
    private final ClienteRepository clienteRepository;

    public void validaSalvar(Atendimento atendimento) {
        validaCamposObrigatorios(atendimento);
        validaSeExisteAtendimentoAtivoComCliente(atendimento);
        validaStatusDoCliente(atendimento);
    }

    public void validaAtualizar(Atendimento atendimento) {
        validaStatusAtendimento(atendimento);
    }

    public void validaDeletar(Atendimento atendimento) {
        validaSeTemVisitasAtivas(atendimento);
    }

    private void validaSeExisteAtendimentoAtivoComCliente(Atendimento atendimento) {
        boolean existeAtivo = atendimentoRepository.existsByCorretorAndClienteAndStatusAtendimentoNotIn(
                atendimento.getCorretor(), atendimento.getCliente(), StatusAtendimentoEnum.statusFinais());

        if (existeAtivo) {
            throw new BusinessException("Já existe atendimento ativo para este cliente e corretor.");
        }
    }

    private void validaCamposObrigatorios(Atendimento atendimento) {
        if (atendimento.getCorretor() == null || atendimento.getCliente() == null || atendimento.getStatusAtendimento() == null) {
            throw new RequiredFieldException("Campos obrigatórios não informados.");
        }
    }

    private void validaStatusDoCliente(Atendimento atendimento) {
        if (atendimento.getCliente().getStatusCliente() == StatusClienteEnum.INATIVO) {
            throw new BusinessException("Não é possível iniciar atendimento para cliente inativo.");
        }
    }

    private void validaSeTemVisitasAtivas(Atendimento atendimento) {
        // Todo: Evoluir para visitas ativas apenas.
        if (atendimento.getVisitas() != null && !atendimento.getVisitas().isEmpty()) {
            throw new BusinessException("Atendimento possui visitas vinculadas");
        }
    }

    private void validaStatusAtendimento(Atendimento atendimento) {
        if (StatusAtendimentoEnum.statusFinais().contains(atendimento.getStatusAtendimento())) {
            throw new BusinessException("Atendimento finalizado não pode ser alterado.");
        }
    }
}
