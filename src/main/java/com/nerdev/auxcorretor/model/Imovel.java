package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.FinalidadeImovelEnum;
import com.nerdev.auxcorretor.model.enums.StatusImovelEnum;
import com.nerdev.auxcorretor.model.enums.TipoImovelEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(
        indexes = {
                @Index(name = "idx_imovel_status", columnList = "statusImovel")
        }
)
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FinalidadeImovelEnum finalidadeImovel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoImovelEnum tipoImovel;

    @Column
    private String cidade;

    @Column
    private String bairro;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusImovelEnum statusImovel;

    @JoinColumn(name = "corretor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Corretor corretorResponsavel;

    @Column
    private int quantidadeVisitas;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDate dataCadastro;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDate dataAtualizacao;

}
