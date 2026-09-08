package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface AtendimentoRepository extends JpaRepository<Atendimento, UUID>, JpaSpecificationExecutor<Atendimento> {

    boolean existsByCorretorAndClienteAndStatusAtendimentoNotIn(
            Corretor corretor,
            Cliente cliente,
            List<StatusAtendimentoEnum> statusFinais
    );


}
