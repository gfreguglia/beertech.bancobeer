package br.com.beertechtalents.lupulo.pocmq.events;

import br.com.beertechtalents.lupulo.pocmq.events.template.NotifyDeposit;
import br.com.beertechtalents.lupulo.pocmq.events.template.NotifyRequestResetPassword;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class RequestChangePasswordEvents {

    public static OutboxEvent createRequestChangePasswordEvents(TokenTrocarSenha token) {
        NotifyRequestResetPassword notifyRequestResetPassword = new NotifyRequestResetPassword(token);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonNode jsonNode = mapper.convertValue(notifyRequestResetPassword, JsonNode.class);
        return new OutboxEvent(token.getId(), "MAIL", jsonNode);
    }
}
