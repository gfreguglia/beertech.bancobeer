package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.UUID;

@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long> {

    Page<Operacao> findByContaUuidOrderByDatahoraDesc(UUID uuid, Pageable pageable);
    Page<Operacao> findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(UUID uuid, Timestamp endDate, Timestamp startDate, Pageable pageable);

}
