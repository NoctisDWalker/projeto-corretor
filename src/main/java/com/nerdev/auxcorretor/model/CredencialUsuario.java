package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(
        name = "auth_credentials",
        uniqueConstraints = { @UniqueConstraint(columnNames = {"providerType", "providerUserId"})},
        indexes = {@Index(name = "idx_usuario_id", columnList = "usuario_id")}
)

public class CredencialUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "usuario_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuario;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderTypeEnum providerType;

    @Column(nullable = false, length = 250)
    private String providerUserId;

    @Column(length = 254)
    private String email;

    @Column(length = 255)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLoginAt;
}
