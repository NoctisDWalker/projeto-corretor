package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.StatusAtendimentoEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = "visitas")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        indexes = {
                @Index(name = "idx_atendimento_corretor", columnList = "corretor_id"),
                @Index(name = "idx_atendimento_cliente", columnList = "cliente_id"),
                @Index(name = "idx_atendimento_status", columnList = "statusAtendimento")
        }
)
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "corretor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Corretor corretor;

    @JoinColumn(name = "cliente_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @OneToMany(
            mappedBy = "atendimento",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Visita> visitas;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusAtendimentoEnum statusAtendimento;

    @Column
    private String observacoes;

    @Column
    private LocalDateTime dataFim;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

}
