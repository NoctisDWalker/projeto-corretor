package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.CompartilhamentoImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImovelValidator {


    public void validaCadastroImovel(Imovel imovel) {
        camposObrigatoriosCreate(imovel);
        validaStatusCriacao(imovel);
    }

    public void validaAtualizar(Imovel imovelPersistido, Imovel imovelAtualizado) {
        validarMudancaStatus(imovelPersistido, imovelAtualizado);
    }

    public void validaInativar(Imovel imovel) {
        if (!imovel.getStatusImovel().podeTransicionarPara(StatusImovelEnum.INATIVO)) {
            throw new BusinessException("Imovel não pode ser cancelada no status atual.");
        }
    }

    public void validaReativar(Imovel imovel) {
        if (!imovel.getStatusImovel().podeTransicionarPara(StatusImovelEnum.DISPONIVEL)) {
            throw new BusinessException("Imóvel não pode ser inativado no status atual.");
        }
    }


    public void validaVisibilidade(Imovel imovelEncontrado, UUID corretorLogado) {
        if (imovelEncontrado.getCorretorResponsavel().getId() == corretorLogado) {
            return;
        }
        if (imovelEncontrado.getStatusImovel().isFinalizado() || imovelEncontrado.getCompartilhamentoImovel() == CompartilhamentoImovelEnum.PRIVADO) {
            throw new BusinessException("Imovel não encontra-se disponivel para visualização");
        }
    }

    private void camposObrigatoriosCreate(Imovel imovel) {
        if (isNullOrBlank(imovel.getTitulo()) || imovel.getValor() == null
                || imovel.getFinalidadeImovel() == null || imovel.getTipoImovel() == null
        ) {
            throw new BusinessException("Todos os campos obrigatorios devem ser preenchidos.");
        }

    }

    private void validaStatusCriacao(Imovel imovel) {
        if (!imovel.getStatusImovel().equals(StatusImovelEnum.DISPONIVEL)) {
            throw new BusinessException("O status de criação do imovel deve ser DISPONIVEL.");
        }
    }

    private boolean isNullOrBlank(String valor) {
        return (valor == null || valor.isBlank());
    }


    private void validarMudancaStatus(Imovel imovelPersistido, Imovel imovelAtualizado) {

        StatusImovelEnum statusAtualizado = imovelAtualizado.getStatusImovel();
        StatusImovelEnum statusPersistido = imovelPersistido.getStatusImovel();

        if (statusAtualizado == null || statusAtualizado.equals(statusPersistido)) {
            return;
        }

        if (!statusPersistido.podeTransicionarPara(statusAtualizado)) {
            throw new BusinessException("Não é permitido alterar status de " + statusPersistido + " para " + statusAtualizado);
        }

    }
}
