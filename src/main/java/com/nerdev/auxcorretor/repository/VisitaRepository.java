package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, UUID>, JpaSpecificationExecutor<Visita> {

    boolean existsByAtendimentoAndDataHoraAgendada(Atendimento atendimento, LocalDateTime dataHoraAgendada);

    boolean existsByAtendimentoAndDataHoraAgendadaAndIdNot(Atendimento atendimento, LocalDateTime dataHoraAgendada, UUID id);
}
