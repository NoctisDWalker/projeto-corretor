package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import com.nerdev.auxcorretor.model.enums.TipoHistoricoVisitaEnum;
import org.springframework.stereotype.Component;

@Component
public class HistoricoVisitaValidator {

    public void validarCriacaoHistorico(
            Visita visita,
            String descricao,
            TipoHistoricoVisitaEnum tipoHistorico,
            StatusVisitaEnum statusSnapshot
    ) {
        validarVisitaObrigatoria(visita);
        validarTipoHistoricoVazioOuNulo(tipoHistorico);
        validarDescricaoVazia(descricao);
        validarTamanhoDescricao(descricao);
        validarStatus(statusSnapshot);
    }

    private void validarStatus(StatusVisitaEnum statusSnapshot) {
        if (isNullObject(statusSnapshot)) {
            throw new BusinessException("Status não pode ser nulo.");
        }
    }

    private void validarTamanhoDescricao(String descricao) {
        if (descricao.length() > 1000 || descricao.length() < 3) {
            throw new BusinessException("Descrição deve ter entre 3 e 1000 caracteres.");
        }
    }

    private void validarDescricaoVazia(String descricao) {
        if (isNullOrBlank(descricao)) {
            throw new BusinessException("Descrição não pode ser vazia ou nula");
        }
    }

    private void validarTipoHistoricoVazioOuNulo(TipoHistoricoVisitaEnum tipoHistorico) {
        if (isNullObject(tipoHistorico)) {
            throw new BusinessException("Tipo do histórico é obrigatório.");
        }
    }

    private void validarVisitaObrigatoria(Visita visita) {
        if (isNullObject(visita)) {
            throw new BusinessException("Visita não pode ser vazia ou nula");
        }
    }

    private boolean isNullOrBlank(String descricao) {
        return descricao == null || descricao.trim().isEmpty();
    }

    private boolean isNullObject(Object obj) {
        return obj == null;
    }

}
