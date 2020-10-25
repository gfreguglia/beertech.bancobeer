package br.com.beertechtalents.lupulo.pocmq.events;

import br.com.beertechtalents.lupulo.pocmq.events.template.NotifyRequestResetPassword;
import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestChangePasswordEvents {

    @Value("${change-password.path}")
    private String path;

    public OutboxEvent createRequestChangePasswordEvents(TokenTrocarSenha token) {
        NotifyRequestResetPassword notifyRequestResetPassword = new NotifyRequestResetPassword(token, path);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JsonNode jsonNode = mapper.convertValue(notifyRequestResetPassword, JsonNode.class);
        return new OutboxEvent(token.getId(), NotifyRequestResetPassword.class, jsonNode);
    }
}
