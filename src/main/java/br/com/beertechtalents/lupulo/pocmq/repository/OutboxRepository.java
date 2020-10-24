package br.com.beertechtalents.lupulo.pocmq.repository;

import br.com.beertechtalents.lupulo.pocmq.model.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox, UUID> {
    Streamable<Outbox> findTop10ByOrderByCreatedOnAsc();
}
