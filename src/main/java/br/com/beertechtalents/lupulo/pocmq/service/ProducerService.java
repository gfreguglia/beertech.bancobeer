package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaOperacaoJms;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.NovaTransferenciaDTO;
import lombok.AllArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProducerService {

    public static final String EXCHANGE_OPERACAO_SAQUE = "operation_saque";
    public static final String EXCHANGE_OPERACAO_DEPOSITO = "operation_deposito";
    public static final String EXCHANGE_TRANSFER = "transfer";

    JmsTemplate jmsTemplate;

    public void sendTransferencia(NovaTransferenciaDTO transferenciaDTO) {
        jmsTemplate.convertAndSend(EXCHANGE_TRANSFER, transferenciaDTO);
    }

    public void sendSaque(NovaOperacaoJms novoSaqueJms) {
        jmsTemplate.convertAndSend(EXCHANGE_OPERACAO_SAQUE, novoSaqueJms);
    }

    public void sendDeposito(NovaOperacaoJms novoSaqueJms) {
        jmsTemplate.convertAndSend(EXCHANGE_OPERACAO_DEPOSITO, novoSaqueJms);
    }
}
