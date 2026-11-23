package com.nerdev.auxcorretor.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "imobiliaria")
@Getter
@Setter
@Builder
@ToString(exclude = "conta")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Imobiliaria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "conta_id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Conta conta;

    @Embedded
    private Endereco endereco;

    @Column(nullable = false)
    private String nomeFantasia;

    @Column(nullable = false)
    private String bio;

    @Column
    private String fotoUrl;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;


}
