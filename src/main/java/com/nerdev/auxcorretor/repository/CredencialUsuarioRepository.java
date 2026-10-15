package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CredencialUsuarioRepository extends JpaRepository<CredencialUsuario, UUID> {

    Optional<CredencialUsuario> findByProviderTypeAndProviderUserId(
            ProviderTypeEnum providerType,
            String providerUserId
    );

    boolean existsByEmail(String email);
    boolean existsByProviderUserIdAndProviderType(
            String providerUserId,
            ProviderTypeEnum providerType
    );

    @Query("SELECT c FROM CredencialUsuario c " +
            "JOIN FETCH c.usuario u " +
            "LEFT JOIN FETCH u.perfilUsuario " +
            "WHERE c.providerType = :providerType AND c.providerUserId = :providerUserId")
    Optional<CredencialUsuario> findByProviderWithUsuarioAndPerfis(
            @Param("providerType") ProviderTypeEnum providerType,
            @Param("providerUserId") String providerUserId
    );

    @Transactional
    @Modifying
    @Query("update CredencialUsuario e set e.lastLoginAt = :valor where e.id = :id")
    void atualizarUltimoLogin(@Param("id") UUID id, @Param("valor") LocalDateTime valor);


}
