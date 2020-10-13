package br.com.beertechtalents.lupulo.pocmq.config;

import br.com.beertechtalents.lupulo.pocmq.controller.TransferenciaController;
import br.com.beertechtalents.lupulo.pocmq.rest.TransacaoAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.UUID;

@EnableRabbit
@Configuration
@AllArgsConstructor
public class RabbitConfig {

    TransacaoAdapter transacaoAdapter;
    TransferenciaController transferenciaController;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public TopicExchange operationExchange() {
        return new TopicExchange("operation", true, false);
    }

    @Bean
    public Queue inboundOperationQueue() {
        return new Queue("operation_inbound", true, false, false);
    }

    @Bean
    public Queue inboundTransferQueue() {
        return new Queue("transfer_inbound", true, false, false);
    }

    @Bean
    public Binding inboundOperationExchangeBinding() {
        return BindingBuilder.bind(inboundOperationQueue()).to(operationExchange()).with("operation");
    }

    @Bean
    public Binding inboundTransferExchangeBinding() {
        return BindingBuilder.bind(inboundTransferQueue()).to(operationExchange()).with("transfer");
    }

    @RabbitListener(queues = {"operation_inbound"})
    public void receive(final String msg) {
        transacaoAdapter.call(msg);
    }

    @RabbitListener(queues = {"transfer_inbound"})
    public void receiveTransfer(final String msg) throws ParseException {
        JSONParser parser = new JSONParser(msg);
        LinkedHashMap<String, Object> obj = parser.parseObject();
        transferenciaController.novaTransferencia(UUID.fromString((String) obj.get("origem")), UUID.fromString((String) obj.get("destino")), new BigDecimal(String.valueOf(obj.get("valor"))));
    }

}
