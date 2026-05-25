package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, UUID> {
}
