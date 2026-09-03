package com.nerdev.auxcorretor.repository.specs;

import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.enums.StatusClienteEnum;
import org.springframework.data.jpa.domain.Specification;

public class ClienteSpecs {

    public static Specification<Cliente> nomeLike(String nome) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("nome")), "%" + nome.toUpperCase() + "%");
    }

    public static Specification<Cliente> telefoneLike(String telefone) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("telefone")), "%" + telefone.toUpperCase() + "%");
    }

    public static Specification<Cliente> emailLike(String email) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("email")), "%" + email.toUpperCase() + "%");
    }

    public static Specification<Cliente> cpfLike(String cpf) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("cpf")), "%" + cpf.toUpperCase() + "%");
    }

    public static Specification<Cliente> statusEquals(StatusClienteEnum status) {
        return (root, query, cb) -> cb.equal(root.get("statusCliente"), status);
    }

}
