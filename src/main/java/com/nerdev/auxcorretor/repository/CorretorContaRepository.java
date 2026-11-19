package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.CorretorConta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CorretorContaRepository extends JpaRepository<CorretorConta, UUID> {

    @Query("""
            SELECT cc FROM CorretorConta cc
            JOIN FETCH cc.conta
            WHERE cc.corretor.id = :corretorId
            AND cc.deletedAt IS NULL                        
            """)
    Optional<CorretorConta> buscarVinculoAtivoPorCorretorId(@Param("corretorId") UUID corretorId);

    boolean existsByCorretorIdAndContaIdAndDeletedAtIsNull(UUID corretorId, UUID contaId);

}
