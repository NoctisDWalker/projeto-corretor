package com.nerdev.auxcorretor.repository;

import com.nerdev.auxcorretor.model.CredencialUsuario;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
