package com.nerdev.auxcorretor.validation;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.repository.CorretorRepository;
import com.nerdev.auxcorretor.repository.ImovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImovelValidator {

    private final ImovelRepository imovelRepository;
    private final CorretorRepository corretorRepository;

    public void validaCadastroImovel(Imovel imovel) {
        camposObrigatoriosCreate(imovel);
        validaStatusCriacao(imovel);
    }

    public void validaAtualizar(Imovel imovelPersistido, Imovel imovelAtualizado, Corretor corretor) {
        validaCorretorResponsavel(imovelPersistido, corretor);
        validarMudancaStatus(imovelPersistido, imovelAtualizado);
    }

    public void validaInativar(Imovel imovel, Corretor corretor) {
        validaCorretorResponsavel(imovel, corretor);
        if (!imovel.getStatusImovel().podeTransicionarPara(StatusImovelEnum.INATIVO)){
            throw new BusinessException("Imovel não pode ser cancelada no status atual.");
        }
    }

    public void validaReativar(Imovel imovel, Corretor corretor) {
        validaCorretorResponsavel(imovel, corretor);
        if (!imovel.getStatusImovel().podeTransicionarPara(StatusImovelEnum.DISPONIVEL)){
            throw new BusinessException("Imóvel não pode ser inativado no status atual.");
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
        if (!imovel.getStatusImovel().equals(StatusImovelEnum.DISPONIVEL)){
            throw new BusinessException("O status de criação do imovel deve ser DISPONIVEL.");
        }
    }

    private boolean isNullOrBlank(String valor) {
        return (valor == null || valor.isBlank());
    }

    private void validaCorretorResponsavel(Imovel imovelPersistido ,Corretor corretor) {
       if(!imovelPersistido.getCorretorResponsavel().getId().equals(corretor.getId())){
           throw new BusinessException("Corretor não é o responsavel pelo imovel");
       }
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
