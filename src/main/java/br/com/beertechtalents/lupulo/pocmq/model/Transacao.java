package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    private BigDecimal valor;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, insertable = false)
    private Timestamp datahora;

}
