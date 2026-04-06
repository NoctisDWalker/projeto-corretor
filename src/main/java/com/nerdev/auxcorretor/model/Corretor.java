package com.nerdev.auxcorretor.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "corretor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Corretor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @ToString.Exclude
    @Column(nullable = false)
    private String senha;

    @Column
    private String telefone;

    @Column(nullable = false)
    private String creci;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "corretor", fetch = FetchType.LAZY)
    private List<Atendimento> atendimentos;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDate dataCadastro;
}