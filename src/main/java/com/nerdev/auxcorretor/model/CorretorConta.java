package com.nerdev.auxcorretor.model;

import com.nerdev.auxcorretor.model.enums.PapelCorretorContaEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "corretor_conta")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"corretor", "conta"})
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CorretorConta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "corretor_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Corretor corretor;

    @JoinColumn(name = "conta_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Conta conta;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PapelCorretorContaEnum papel;

    @Column(nullable = false)
    private String emailProfissional;

    @Column
    private String telefoneProfissional;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

}
