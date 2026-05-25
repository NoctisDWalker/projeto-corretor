package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

}
