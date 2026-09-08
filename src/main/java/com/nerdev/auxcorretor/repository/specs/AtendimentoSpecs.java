package com.nerdev.auxcorretor.repository.specs;

import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class AtendimentoSpecs {

    public static Specification<Atendimento> idClienteEquals(UUID idCliente) {
        return (root, query, cb) -> cb.equal(root.get("idCliente").get("id"), idCliente);
    }

    public static Specification<Atendimento> idCorretorEquals(UUID idCorretor) {
        return (root, query, cb) -> cb.equal(root.get("idCorretor").get("id"), idCorretor);
    }

    public static Specification<Atendimento> statusAtendimentoEquals(StatusAtendimentoEnum statusAtendimento) {
        return (root, query, cb) -> cb.equal(root.get("statusAtendimento"), statusAtendimento);
    }

    public static Specification<Atendimento> menorDataCadastro(LocalDate menorDataCadastro) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataCadastro"), menorDataCadastro.atStartOfDay());
    }

    public static Specification<Atendimento> maiorDataCadastro(LocalDate maiorDataCadastro) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataCadastro"), maiorDataCadastro.atTime(LocalTime.MAX));
    }

    public static Specification<Atendimento> menorDataFim(LocalDate menorDataFim) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataFim"), menorDataFim.atStartOfDay());
    }

    public static Specification<Atendimento> maiorDataFim(LocalDate maiorDataFim) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataFim"), maiorDataFim.atTime(LocalTime.MAX));
    }
}
