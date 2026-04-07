package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Corretor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorretorRepository extends JpaRepository<Corretor, UUID> {

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByCreci(String creci);

    Optional<Corretor> findByEmail(String email);
    Optional<Corretor> findByCpf(String cpf);
    Optional<Corretor> findByCreci(String creci);
    Optional<Corretor> findByEmailAndAtivoTrue(String email);

    List<Corretor> findAllByAtivoTrue(boolean ativo);
}
