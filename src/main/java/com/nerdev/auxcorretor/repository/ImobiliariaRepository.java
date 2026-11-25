package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Imobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImobiliariaRepository extends JpaRepository<Imobiliaria, UUID> {
}
