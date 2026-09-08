package com.nerdev.auxcorretor.repository.specs;

import com.nerdev.auxcorretor.model.Imovel;
import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class ImovelSpecs {

    public static Specification<Imovel> tituloLike(String titulo) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");
    }

    public static Specification<Imovel>  valorMinimo(BigDecimal valorMinimo) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("valor"), valorMinimo);
    }

    public static Specification<Imovel>  valorMaximo(BigDecimal valorMaximo) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("valor"), valorMaximo);
    }

    public static Specification<Imovel>  finalidadeEquals(FinalidadeImovelEnum finalidadeImovel) {
        return (root, query, cb) -> cb.equal(root.get("finalidadeImovel"), finalidadeImovel);
    }

    public static Specification<Imovel>  tipoEquals(TipoImovelEnum tipoImovel) {
        return (root, query, cb) -> cb.equal(root.get("tipoImovel"), tipoImovel);
    }

    public static Specification<Imovel> cidadeLike(String cidade) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("cidade")), "%" + cidade.toUpperCase() + "%");
    }

     public static Specification<Imovel> bairroLike(String bairro) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("bairro")), "%" + bairro.toUpperCase() + "%");
    }

    public static Specification<Imovel> statusEquals(StatusImovelEnum statusImovel) {
        return (root, query, cb) -> cb.equal(root.get("statusImovel"), statusImovel);
    }

    public static Specification<Imovel> corretorResponsavelEquals(UUID  corretorResponsavelId) {
        return (root, query, cb) -> cb.equal(root.get("corretorResponsavel").get("id"), corretorResponsavelId);
    }

    public static Specification<Imovel> dataCadastroInicial(LocalDate dataCadastroInicial) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataCadastro"), dataCadastroInicial.atStartOfDay());
    }

    public static Specification<Imovel> dataCadastroFinal(LocalDate dataCadastroFinal) {
        return (root, query, cb) -> cb.equal(root.get("dataCadastro"), dataCadastroFinal.atTime(LocalTime.MAX));
    }
}
