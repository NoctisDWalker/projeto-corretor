package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImovelRepository extends JpaRepository<Imovel, UUID> {
}
