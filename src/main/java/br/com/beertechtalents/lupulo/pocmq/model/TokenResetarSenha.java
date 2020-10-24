package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Data
@Getter
public class TokenResetarSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Timestamp expiraEm;

    private Conta conta;

    private final long TEMPO_PARA_EXPERIAR_EM_MINUTOS = 60;

    public TokenResetarSenha(Conta conta) {
        this.conta = conta;

        this.expiraEm = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(TEMPO_PARA_EXPERIAR_EM_MINUTOS));
    }

    public boolean hasExpired() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return expiraEm.compareTo(now) < 0;
    }
}
