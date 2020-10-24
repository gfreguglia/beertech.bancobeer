package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TokenTrocarSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    @Column(unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid = UUID.randomUUID();

    @NotNull
    private Timestamp expiraEm;

    @CreatedDate
    private Timestamp criadoEm;

    @NotNull
    @ManyToOne
    @JoinColumn(
            name = "conta_uuid",
            referencedColumnName = "uuid",
            columnDefinition = "BINARY(16)"
    )
    private Conta conta;

    @NotNull
    private boolean invalido;

    private static final long TEMPO_PARA_EXPERIAR_EM_MINUTOS = 60;

    public TokenTrocarSenha(Conta conta) {
        this.conta = conta;
        this.invalido = false;

        this.expiraEm = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(TEMPO_PARA_EXPERIAR_EM_MINUTOS));
    }

    public boolean hasExpired() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return expiraEm.compareTo(now) < 0;
    }

    public void invalidar() {
        this.invalido = true;
    }

    public static Timestamp getOldestValidTimestamp() {
        return new Timestamp(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(TEMPO_PARA_EXPERIAR_EM_MINUTOS));
    }
}
