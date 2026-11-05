package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID>, JpaSpecificationExecutor<Cliente> {

    Optional<Cliente> findByEmailAndCorretorId(String email, UUID corretorId);
    Optional<Cliente> findByCpfAndCorretorId(String cpf, UUID corretorId);
    Optional<Cliente> findByIdAndCorretorId(UUID id, UUID corretorlId);

}
