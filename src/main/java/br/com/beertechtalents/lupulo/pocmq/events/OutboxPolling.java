package br.com.beertechtalents.lupulo.pocmq.events;

import br.com.beertechtalents.lupulo.pocmq.events.template.NotifyDeposit;
import br.com.beertechtalents.lupulo.pocmq.model.Outbox;
import br.com.beertechtalents.lupulo.pocmq.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

@Slf4j
@Component
@AllArgsConstructor
public class OutboxPolling {

    OutboxRepository repository;
    RabbitTemplate template;

    @Scheduled(fixedDelay = 5000)
    public void pendingOutbox() {
        ObjectMapper mapper = new ObjectMapper();
        Streamable<Outbox> outboxes = repository.findTop10ByOrderByCreatedOnAsc();
        outboxes.forEach(outbox -> {
            if (NotifyDeposit.class.equals(outbox.getEventType())) {
                try {
                    template.send("send-email",
                            MessageBuilder
                                    .withBody(outbox.getPayload().getBytes())
                                    .setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                                    .build());
                    repository.delete(outbox);
                    log.debug("Message sent to RabbitMQ: {}", outbox.getUuid());
                } catch (AmqpException e) {
                    log.warn("Error publishing message to RabbitMQ, retrying in 5s: {}", e.getLocalizedMessage());
                }
            } else {
                log.warn("Unhandled eventType: {}", outbox.getEventType());
                return;
            }
        });
    }

}
