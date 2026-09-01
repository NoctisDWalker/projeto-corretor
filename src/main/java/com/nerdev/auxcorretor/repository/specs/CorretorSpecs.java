package com.nerdev.auxcorretor.repository.specs;

import com.nerdev.auxcorretor.model.Corretor;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CorretorSpecs {

    public static Specification<Corretor> idEqual(UUID id) {
        return (root, query, cb) -> cb.equal(root.get("id"), id);
    }

    public static Specification<Corretor> nomeLike(String nome) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("nome")), "%" + nome.toUpperCase() + "%");

    }

    public static Specification<Corretor> cpfLike(String cpf) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("cpf")), "%" + cpf.toUpperCase() + "%");
    }

    public static Specification<Corretor> creciLike(String creci) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("creci")), "%" + creci.toUpperCase());
    }

}