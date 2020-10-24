package br.com.beertechtalents.lupulo.pocmq.events;

import br.com.beertechtalents.lupulo.pocmq.model.Outbox;
import br.com.beertechtalents.lupulo.pocmq.repository.OutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {

    OutboxRepository outboxRepository;

    @EventListener
    public void handleOutboxEvent(OutboxEvent outboxEvent) {
        UUID uuid = UUID.randomUUID();
        Outbox outbox = new Outbox(uuid,
                outboxEvent.getAggregateId(),
                outboxEvent.getEventType(),
                outboxEvent.getPayload().toString(),
                new Date());

        log.debug("Handling event: {}.", outbox);

        outboxRepository.save(outbox);
    }
}
