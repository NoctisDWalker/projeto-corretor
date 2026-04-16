package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.repository.VisitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VisitaValidator {

    private final VisitaRepository visitaRepository;

    public void validaCadastro(Visita visita, Atendimento atendimento) {
        camposObrigatoriosCreate(visita);
        validaStatusAtendimento(atendimento);
        validaDataHora(visita);
        validarDuplicidadeHorario(atendimento, visita);
    }

    public void validaAtualizar(Visita visitaPersistida, Visita visitaAtualizada, Atendimento atendimento) {
        validarStatusVisitaFinalizado(visitaPersistida);
        validarMudancaStatus(visitaPersistida.getStatusVisita(), visitaAtualizada.getStatusVisita());
        validaDataHora(visitaAtualizada);
        validarDuplicidadeHorario(atendimento, visitaAtualizada);
    }


    public void validaDeletar(Visita visita) {
        validarStatusVisitaFinalizado(visita);
        validaStatusAtendimento(visita.getAtendimento());
    }

    private void validaStatusAtendimento(Atendimento atendimento) {
        if (atendimento.getStatusAtendimento().isFinalizado()) {
            throw new BusinessException("Não é possível agendar visita para atendimento finalizado.");
        }
    }

    private void validaDataHora(Visita visita) {
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        if (visita.getDataHoraAgendada() == null) {
            throw new BusinessException("A data e hora da visita devem ser informadas.");
        }
        if (visita.getDataHoraAgendada().isBefore(dataHoraAtual)) {
            throw new BusinessException("Data da visita deve ser futura.");
        }
    }

    private void validarDuplicidadeHorario(Atendimento atendimento, Visita visita) {
        boolean existeHorario;
        if (visita.getId() == null) {
            existeHorario = visitaRepository.existsByAtendimentoAndDataHoraAgendada(atendimento, visita.getDataHoraAgendada());
        } else {
            existeHorario = visitaRepository.existsByAtendimentoAndDataHoraAgendadaAndIdNot(atendimento, visita.getDataHoraAgendada(), visita.getId());
        }
        if(existeHorario){
            throw new BusinessException("Já existe uma visita agendada para esta data e hora.");
        }
    }

    private void camposObrigatoriosCreate(Visita visita) {

        if (visita.getImovel() == null || visita.getDataHoraAgendada() == null) {
            throw new BusinessException("Todos os campos Imovel e DataHoraAgendada devem ser preenchidos.");
        }

    }

    private void validarStatusVisitaFinalizado(Visita visita) {
        if (visita.getStatusVisita().isFinalizado()) {
            throw new BusinessException("Visitas finalizadas não podem ser alteradas");
        }
    }

    private void validarMudancaStatus(StatusVisitaEnum statusAtual, StatusVisitaEnum statusNovo) {

        if (statusNovo == null) {
            return;
        }

        if (statusNovo.equals(statusAtual)) {
            throw new BusinessException("Visita já está neste status");
        }

        if (!statusAtual.podeTransicionarPara(statusNovo)) {
            throw new BusinessException("Não é permitido alterar status de " + statusAtual + " para " + statusNovo);
        }
    }

}