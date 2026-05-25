package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.CorretorConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CorretorContaRepository extends JpaRepository<CorretorConta, UUID> {
}
