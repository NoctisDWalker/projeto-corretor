package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.exception.BusinessException;
import com.nerdev.auxcorretor.model.enums.PerfilUsuarioEnum;
import com.nerdev.auxcorretor.model.enums.ProviderTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "auth_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "credenciais")
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<CredencialUsuario> credenciais = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PerfilUsuarioEnum perfilUsuario;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Column
    LocalDateTime deletedAt;

    public Set<CredencialUsuario> getCredenciais() {
        return Collections.unmodifiableSet(credenciais);
    }

    public void adicionarCredencial(CredencialUsuario credencial) {
        Objects.requireNonNull(credencial, "Credencial não pode ser nula!");

        boolean existeCredencial = this.credenciais.stream()
                .anyMatch(c -> c.getProviderType().equals(credencial.getProviderType())
                        && c.getProviderUserId().equals(credencial.getProviderUserId()));

        if (existeCredencial) {
            throw new BusinessException("Credencial ja existente!");
        }

        credencial.setUsuario(this);
        this.credenciais.add(credencial);
    }

    public void removerCredencial(CredencialUsuario credencial) {
        if (this.credenciais.size() == 1) {
            throw new BusinessException("Usuario não pode ficar sem credenciais!");
        }
        if (!this.credenciais.contains(credencial)) {
            throw new BusinessException("Credencial não encontrada para o usuario.");
        }
        this.credenciais.remove(credencial);
        credencial.setUsuario(null);
    }

    public boolean possuiProvider(ProviderTypeEnum provider) {
        return this.credenciais.stream().anyMatch(c -> c.getProviderType().equals(provider));
    }

    public Optional<CredencialUsuario> buscarCredencial(ProviderTypeEnum provider) {
        return this.credenciais.stream()
                .filter(c -> c.getProviderType().equals(provider)).findFirst();
    }

    public void deletar() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean estaAtivo() {
        return this.deletedAt == null;
    }

}
