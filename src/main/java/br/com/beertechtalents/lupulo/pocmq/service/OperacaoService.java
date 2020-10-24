package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.controller.exception.BusinessValidationException;
import br.com.beertechtalents.lupulo.pocmq.controller.exception.EntityNotFoundException;
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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
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
        Conta conta = contaRepository.findByUuid(operacao.getConta().getUuid())
                .orElseThrow(() -> new EntityNotFoundException(Conta.class, "UUID: %s", operacao.getConta().getUuid()));
        final BigDecimal saldo = contaService.computeSaldo(conta);
        switch (operacao.getTipo()) {
            case SAQUE:
                if (saldo.compareTo(operacao.getValor()) >= 0) {
                    operacao.setSaldoAtual(saldo.subtract(operacao.getValor()));
                    salvarSaque(operacao);
                    break;
                } else {
                    throw new BusinessValidationException("Insufficient funds");
                }
            case DEPOSITO:
                operacao.setSaldoAtual(saldo.add(operacao.getValor()));
                salvarDeposito(operacao);
                break;
            default:
                throw new BusinessValidationException("Invalid operation type");
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

    public Page<Operacao> getPageExtrato(UUID uuid, LocalDate inicio, LocalDate fim, int page, int size) {


        Timestamp inicioQuery = getInicioQuery(inicio);
        Timestamp fimQuery = getFimQuery(fim);

        if (inicioQuery.after(fimQuery)) {
            throw new BusinessValidationException("A data de inicio nao pode ser maior que a data de fim");
        }

        Pageable pageable = PageRequest.of(page, size);
        return operacaoRepository.findByContaUuidAndDatahoraBetweenOrderByDatahoraDesc(uuid, inicioQuery, fimQuery, pageable);
    }


    private Timestamp getInicioQuery(LocalDate inicio) {
        Timestamp inicioQuery;

        if (inicio == null) {
            inicio = LocalDate.now().minusDays(15);
        }
        return Timestamp.valueOf(inicio.atStartOfDay());
    }

    private Timestamp getFimQuery(LocalDate fim) {
        Timestamp inicioQuery;

        if (fim == null) {
            fim = LocalDate.now();
        }
        return Timestamp.valueOf(fim.atTime(LocalTime.MAX));
    }
}
