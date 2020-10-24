package br.com.beertechtalents.lupulo.pocmq.events;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutboxEvent {

    private Long aggregateId;
    private Class<?> eventType;
    private JsonNode payload;
}
