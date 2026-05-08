package com.nerdev.auxcorretor.service;

import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.enums.TipoHistoricoVisitaEnum;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import com.nerdev.auxcorretor.repository.HistoricoVisitaRepository;
import com.nerdev.auxcorretor.validation.HistoricoVisitaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoricoVisitaService {

    private final HistoricoVisitaRepository historicoVisitaRepository;
    private final HistoricoVisitaValidator historicoVisitaValidator;

    public void criarHistoricoAutomatico(Visita visita, String descricao) {
        registrarHistorico(visita, descricao, TipoHistoricoVisitaEnum.AUTOMATICO);
    }
    public void criarHistoricoManual(Visita visita, String descricao) {
        registrarHistorico(visita, descricao, TipoHistoricoVisitaEnum.MANUAL);
    }

    private void registrarHistorico(Visita visita, String descricao, TipoHistoricoVisitaEnum tipo) {

        StatusVisitaEnum status = visita.getStatusVisita();

        historicoVisitaValidator.validarCriacaoHistorico(
                visita,
                descricao,
                tipo,
                status
        );

        HistoricoVisita historicoVisita = HistoricoVisita.builder()
                .visita(visita)
                .descricao(descricao)
                .statusVisitaAtual(visita.getStatusVisita())
                .tipoHistorico(tipo)
                .build();

        historicoVisitaRepository.save(historicoVisita);
    }

}