package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.exception.BusinessException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "corretor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "corretorContas"})
@EntityListeners(AuditingEntityListener.class)
public class Corretor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "usuario_id", nullable = false)
    @OneToOne
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "corretor", fetch = FetchType.LAZY)
    private Set<CorretorConta> corretorContas = new HashSet<>();

    @Column(nullable = false)
    private String nome;

    @Column
    private String cpf;

    @Column
    private String bio;

    // TODO: Remover campo na refatoração do cadastro de corretor.
    // O e-mail passou a ser responsabilidade de CredencialUsuario.
    @Column(unique = true)
    private String email;

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

    public boolean pertenceAConta(Conta conta) {
        return corretorContas.stream()
                .anyMatch(c -> c.getDeletedAt() == null
                        && c.getConta().getId().equals(conta.getId()));
    }

}