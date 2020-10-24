package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.events.EventPublisher;
import br.com.beertechtalents.lupulo.pocmq.events.OperationEvents;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository operacaoRepository;
    final RabbitTemplate rabbitTemplate;
    final ContaRepository contaRepository;
    final ContaService contaService;
    final EventPublisher eventPublisher;

    @Transactional
    public void salvarOperacao(Operacao operacao) {
        Conta conta = contaRepository.findByUuid(operacao.getConta().getUuid()).get();
        switch (operacao.getTipo()) {
            case SAQUE:
                if (contaService.computeSaldo(conta).compareTo(operacao.getValor()) >= 0) {
                    salvarSaque(operacao);
                    break;
                } else {
                    throw new HttpClientErrorException(HttpStatus.PRECONDITION_FAILED, "Insufficient funds");
                }
            case DEPOSITO:
                salvarDeposito(operacao);
                break;
            default:
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid operation type");
        }

    }

    @Transactional
    public void salvarDeposito(Operacao operacao) {
        operacaoRepository.save(operacao);
        eventPublisher.fire(OperationEvents.createDepositEvent(operacao));
    }

    private void salvarSaque(Operacao operacao) {
        operacaoRepository.save(operacao);
    }

    public Page<Operacao> getPageOperacao(UUID uuid, int page, int size) {
        return operacaoRepository.findByContaUuidOrderByDatahoraDesc(uuid, PageRequest.of(page, size));
    }

    public Page<Operacao> getPageExtrato(UUID uuid, Timestamp inicio, Timestamp fim, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return operacaoRepository.findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(uuid, inicio, fim, pageable);
    }
}
