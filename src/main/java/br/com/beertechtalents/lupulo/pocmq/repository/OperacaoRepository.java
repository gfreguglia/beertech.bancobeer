package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

    @Query("Select SUM(t.valor) FROM Operacao t")
    BigDecimal somaSaldo();
}
