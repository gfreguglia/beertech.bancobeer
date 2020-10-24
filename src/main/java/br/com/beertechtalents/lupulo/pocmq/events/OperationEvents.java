package br.com.beertechtalents.lupulo.pocmq.events;

import br.com.beertechtalents.lupulo.pocmq.events.template.NotifyDeposit;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public abstract class OperationEvents {
    public static OutboxEvent createDepositEvent(Operacao operacao) {
        NotifyDeposit notifyDeposit = new NotifyDeposit(operacao);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonNode jsonNode = mapper.convertValue(notifyDeposit, JsonNode.class);
        return new OutboxEvent(operacao.getId(), "MAIL", jsonNode);
    }
}
