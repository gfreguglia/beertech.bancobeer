package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContaRepository extends MongoRepository<Conta, String> {
    Optional<Conta> findByUuid(UUID uuid);

    Optional<Conta> findByEmail(String email);
}
