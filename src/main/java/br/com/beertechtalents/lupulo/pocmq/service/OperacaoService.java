package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository operacaoRepository;

    @PreAuthorize("isAuthenticated()")
    public void salvarOperacao(Operacao operacao) {

        if (operacao.getTipoMovimentacao() == null) {
            operacao.setTipoMovimentacao(operacao.getTipo());
        }

        switch (operacao.getTipo()) {
            case SAQUE:
                salvarSaque(operacao);
                break;
            case DEPOSITO:
                salvarDeposito(operacao);
                break;
            default:
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid operation type");
        }

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    private void salvarDeposito(Operacao operacao) {
        operacaoRepository.save(operacao);
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
