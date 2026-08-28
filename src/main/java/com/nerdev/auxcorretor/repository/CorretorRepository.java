package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Corretor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, UUID> {

    Optional<Corretor> findByCreci(String creci);
    boolean existsByCreci(String cresci);
    boolean existsByCpf(String cpf);
}
