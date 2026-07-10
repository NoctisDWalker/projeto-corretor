package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {

    Optional<Conta> findByEmail(String email);

    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByDocumentoIgnoreCase(String documento);

}
