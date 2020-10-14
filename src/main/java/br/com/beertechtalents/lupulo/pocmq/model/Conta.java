package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Conta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String nome;

    // Length deve ser 16 pois o default do hibernate é 255 e o UUID usa 16
    @NaturalId
    @Column(unique = true, columnDefinition = "BINARY(16)")
    UUID uuid = UUID.randomUUID();

    @CreatedDate
    Timestamp criadoEm;

    @OneToMany(mappedBy = "conta")
    List<Operacao> operacoes = new ArrayList<>();

}
