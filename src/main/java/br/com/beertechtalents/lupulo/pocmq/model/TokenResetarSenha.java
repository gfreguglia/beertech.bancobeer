package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Getter
public class TokenResetarSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private Timestamp expiraEm;

    @Column(nullable = false)
    private Conta conta;

    @Column(nullable = false)
    private boolean usado;

    private final long TEMPO_PARA_EXPERIAR_EM_MINUTOS = 60;

    public TokenResetarSenha(Conta conta) {
        this.conta = conta;
        this.usado = false;

        this.expiraEm = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(TEMPO_PARA_EXPERIAR_EM_MINUTOS));
    }

    public boolean hasExpired() {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        return expiraEm.compareTo(now) < 0;
    }

    public void usar() {
        this.usado = true;
    }
}
