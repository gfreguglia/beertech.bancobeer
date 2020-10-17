package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByUuid(UUID uuid);
    Optional<Conta> findByEmail(String email);
}
