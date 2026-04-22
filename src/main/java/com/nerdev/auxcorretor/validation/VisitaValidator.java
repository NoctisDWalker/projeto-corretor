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
        validaStatusCriacao(visita.getStatusVisita());
        validaStatusAtendimento(atendimento);
        validaDataHora(visita);
        validarDuplicidadeHorario(atendimento, visita);
    }

    public void validaAtualizar(Visita visitaPersistida, Visita visitaAtualizada, Atendimento atendimento) {
        validarStatusVisitaFinalizado(visitaPersistida);
        validarMudancaStatus(visitaPersistida, visitaAtualizada);
        StatusVisitaEnum statusFinal = visitaAtualizada.getStatusVisita() != null
                ? visitaAtualizada.getStatusVisita()
                : visitaPersistida.getStatusVisita();
        if (!statusFinal.isFinalizado()) {
            validaDataHora(visitaAtualizada);
            validarDuplicidadeHorario(atendimento, visitaAtualizada);
        }
    }


    public void validaCancelar(Visita visita) {
        validaStatusAtendimento(visita.getAtendimento());
        if (!visita.getStatusVisita().podeTransicionarPara(StatusVisitaEnum.CANCELADA)){
            throw new BusinessException("Visita não pode ser cancelada no status atual.");
        }
    }

    public void validaReativar(Visita visita) {
        validaStatusAtendimento(visita.getAtendimento());
        if (!visita.getStatusVisita().podeTransicionarPara(StatusVisitaEnum.REMARCADA)) {
            throw new BusinessException("Visita não pode ser reativada no status atual.");
        }
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
        if (existeHorario) {
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
            throw new BusinessException("Visitas finalizadas não podem sofrer alterações.");
        }
    }

    private void validaStatusCriacao(StatusVisitaEnum status) {
        if (!status.equals(StatusVisitaEnum.AGENDADA)) {
            throw new BusinessException("Visita deve ser criada com o status agendado");
        }
    }

    private void validarMudancaStatus(Visita visitaPersistida, Visita visitaAtualizada) {

        StatusVisitaEnum statusAtual = visitaPersistida.getStatusVisita();
        StatusVisitaEnum statusNovo = visitaAtualizada.getStatusVisita();

        if (statusNovo == null || statusNovo.equals(statusAtual)) {
            return;
        }

        if (!statusAtual.podeTransicionarPara(statusNovo)) {
            throw new BusinessException("Não é permitido alterar status de " + statusAtual + " para " + statusNovo);
        }

        if (statusNovo.isFinalizado()) {
            validaDataHoraRealizada(visitaAtualizada, visitaAtualizada.getDataHoraRealizada());
        }
    }

    private void validaDataHoraRealizada(Visita visita, LocalDateTime dataHoraRealizada) {
        if (visita.getDataHoraRealizada() == null) {
            throw new BusinessException("A Data/Hora realizada, deve ser informada");
        }
        if (dataHoraRealizada.isAfter(LocalDateTime.now())) {
            throw new BusinessException("A Data/Hora realizada, deve ser anterior ao momento atual");
        }
        if (dataHoraRealizada.isBefore(visita.getDataHoraAgendada())) {
            throw new BusinessException("A Data/Hora realizada, deve ser posterior a data/hora da visita agendada");
        }
    }

}