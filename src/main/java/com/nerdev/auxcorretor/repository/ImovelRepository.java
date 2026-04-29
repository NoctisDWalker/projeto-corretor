package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Imovel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImovelRepository extends JpaRepository<Imovel, UUID> {

    List<Imovel> findByCorretorResponsavelId(UUID corretorResponsavelId);

}
