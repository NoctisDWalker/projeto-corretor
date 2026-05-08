package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Visita;
import com.nerdev.auxcorretor.model.historicos.HistoricoVisita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoricoVisitaRepository  extends JpaRepository<HistoricoVisita, UUID> {

    List<HistoricoVisita> findByVisitaIdOrderByDataHoraCadastroAsc(UUID id);

    @Query("""
       SELECT v
       FROM Visita v
       LEFT JOIN FETCH v.historicos h
       WHERE v.id = :id
       ORDER BY h.dataHoraCadastro ASC
       """)
    Optional<Visita> findByIdComHistoricoOrdenado(UUID id);

}
