package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenResetarSenhaRepository extends JpaRepository<TokenTrocarSenha, Long> {

    Optional<TokenTrocarSenha> findByUuid(UUID uuid);

    @Modifying
    @Query("update TokenTrocarSenha token set token.invalido = true where token.conta.uuid = ?1 and token.expiraEm > ?2")
    void invalidarTokens (UUID contaUuid, Timestamp timestamp);
}
