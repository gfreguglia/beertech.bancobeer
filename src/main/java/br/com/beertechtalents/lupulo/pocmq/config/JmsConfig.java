package br.com.beertechtalents.lupulo.pocmq.config;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoJms;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.service.OperacaoService;
import br.com.beertechtalents.lupulo.pocmq.service.TransferenciaService;
import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.Queue;
import javax.transaction.Transactional;
import java.util.Optional;

@EnableJms
@Configuration
@AllArgsConstructor
public class JmsConfig {

    TransferenciaService transferenciaService;
    OperacaoService operacaoService;
    ContaService contaService;

    @Bean
    public Queue inboundOperationSaqueQueue() {
        return new ActiveMQQueue("operation_saque");
    }

    @Bean
    public Queue inboundOperationDepositoQueue() {
        return new ActiveMQQueue("operation_deposito");
    }

    @Bean
    public Queue inboundTransferQueue() {
        return new ActiveMQQueue("transfer");
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }

    @Transactional
    @JmsListener(destination = "operation_saque")
    public void receiveSaque(NovaOperacaoJms msg) {
        Operacao op = prepareOp(msg);

        op.setTipo(Operacao.TipoTransacao.SAQUE);
        op.setDescricaoOperacao(Operacao.DescricaoOperacao.SAQUE);

        operacaoService.salvarOperacao(op);
    }

    @Transactional
    @JmsListener(destination = "operation_deposito")
    public void receiveDeposito(NovaOperacaoJms msg) {
        Operacao op = prepareOp(msg);

        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
        op.setDescricaoOperacao(Operacao.DescricaoOperacao.DEPOSITO);

        operacaoService.salvarOperacao(op);
    }

    private Operacao prepareOp(NovaOperacaoJms msg) {
        Operacao op = new Operacao();
        msg.setValor(msg.getValor().abs());
        Optional<Conta> optionalConta = contaService.getConta(msg.getConta());

        if (optionalConta.isEmpty()) {
            throw new RuntimeException();
        }

        op.setConta(optionalConta.get());
        op.setValor(msg.getValor());
        return op;
    }

    @Transactional
    @JmsListener(destination = "transfer")
    public void receiveTransfer(final NovaTransferenciaDTO dto) {
        transferenciaService.transferir(dto.getOrigem(), dto.getDestino(), dto.getValor());
    }

}
