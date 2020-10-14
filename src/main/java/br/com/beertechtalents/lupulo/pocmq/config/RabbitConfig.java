package br.com.beertechtalents.lupulo.pocmq.config;

import br.com.beertechtalents.lupulo.pocmq.controller.OperacaoController;
import br.com.beertechtalents.lupulo.pocmq.controller.TransferenciaController;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoDTO;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@AllArgsConstructor
public class RabbitConfig {

    TransferenciaController transferenciaController;
    OperacaoController operacaoController;

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

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @RabbitListener(queues = {"operation_inbound"})
    public void receive(final NovaOperacaoDTO dto) {
        operacaoController.novaOperacao(dto);
    }

    @RabbitListener(queues = {"transfer_inbound"})
    public void receiveTransfer(final NovaTransferenciaDTO dto) {
        transferenciaController.novaTransferencia(dto);
    }

}
