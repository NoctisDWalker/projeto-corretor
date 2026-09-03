package com.nerdev.auxcorretor.repository.specs;

import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.enums.InteresseClienteEnum;
import com.nerdev.auxcorretor.model.enums.StatusVisitaEnum;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class VisitaSpecs {

    public static Specification<Visita> idAtendimentoEquals(UUID idAtendimento) {
        return (root, query, cb) -> cb.equal(root.get("atendimento").get("id"), idAtendimento);
    }

    public static Specification<Visita> idImovelEquals(UUID idImovel) {
        return (root, query, cb) -> cb.equal(root.get("imovel").get("id"), idImovel);
    }

    public static Specification<Visita> menorDataAgendada(LocalDate menorDataAgendada) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataHoraAgendada"), menorDataAgendada.atStartOfDay());
    }

    public static Specification<Visita> maiorDataAgendada(LocalDate maiorDataAgendada) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataHoraAgendada"), maiorDataAgendada.atTime(LocalTime.MAX));
    }

    public static Specification<Visita> menorDataRealizada(LocalDate menorDataRealizada) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataHoraRealizada"), menorDataRealizada.atStartOfDay());
    }

    public static Specification<Visita> maiorDataRealizada(LocalDate maiorRealizada) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataHoraRealizada"), maiorRealizada.atTime(LocalTime.MAX));
    }

    public static Specification<Visita> statusVisitaEqual(StatusVisitaEnum statusVisita) {
        return (root, query, cb) -> cb.equal(root.get("statusVisita"), statusVisita);
    }

    public static Specification<Visita> interesseClienteEqual(InteresseClienteEnum interesseCliente) {
        return (root, query, cb) -> cb.equal(root.get("interesseCliente"), interesseCliente);
    }

    public static Specification<Visita> menorDataCadastro(LocalDate menorDataCadastro) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataCadastro"), menorDataCadastro.atStartOfDay());
    }

    public static Specification<Visita> maiorDataCadastro(LocalDate maiorDataCadastro) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataCadastro"), maiorDataCadastro.atTime(LocalTime.MAX));
    }
}