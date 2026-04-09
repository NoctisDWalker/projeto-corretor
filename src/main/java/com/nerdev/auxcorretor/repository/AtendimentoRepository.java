package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.Atendimento;
import com.nerdev.auxcorretor.model.Cliente;
import com.nerdev.auxcorretor.model.Corretor;
import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AtendimentoRepository extends JpaRepository<Atendimento, UUID> {

    boolean existsByCorretorAndClienteAndStatusAtendimentoNotIn(
            Corretor corretor,
            Cliente cliente,
            List<StatusAtendimentoEnum> statusFinais
    );


}
