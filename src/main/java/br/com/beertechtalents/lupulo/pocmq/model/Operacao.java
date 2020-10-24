package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Operacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DescricaoOperacao descricaoOperacao;

    @Column
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private BigDecimal saldoAtual;

    @ManyToOne
    @JoinColumn(
            name = "conta_uuid",
            referencedColumnName = "uuid",
            columnDefinition = "BINARY(16)"
    )
    private Conta conta;

    @CreatedDate
    private Timestamp datahora;

    public enum TipoTransacao {
        DEPOSITO,
        SAQUE
    }

    public enum DescricaoOperacao {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA_ORIGEM,
        TRANSFERENCIA_DESTINO,
    }

    public enum Categoria {
        ALIMENTACAO,
        SAUDE,
        TRANSPORTE,
        ENTRETENIMENTO,
    }

}
