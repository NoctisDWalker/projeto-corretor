package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ContaRepository extends JpaRepository<Conta, UUID> {
}
