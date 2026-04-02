package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByEmailAndCorretorId(String email, UUID correorId);
    Optional<Cliente> findByNomeAndCorretorId(String nome, UUID correorId);
    List<Cliente> findAllByCorretorId(UUID corretorId);

}
