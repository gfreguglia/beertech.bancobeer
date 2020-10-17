package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProducerService {

    public static final String  EXCHANGE_OPERACAO = "operation";
    public static final String  ROUTING_KEY_OPERACAO = "operation";
    public static final String ROUTING_KEY_TRANSFERENCIA = "transfer";

    RabbitTemplate rabbitTemplate;

    public void sendOperacao(NovaOperacaoDTO operacaoDTO) {
        sendMessage(EXCHANGE_OPERACAO,ROUTING_KEY_OPERACAO,operacaoDTO);
    }

    public void sendTransferencia(NovaTransferenciaDTO transferenciaDTO) {
        sendMessage(EXCHANGE_OPERACAO,ROUTING_KEY_TRANSFERENCIA,transferenciaDTO);
    }

    private void sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }

}
