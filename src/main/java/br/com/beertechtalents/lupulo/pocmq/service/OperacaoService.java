package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository operacaoRepository;

    @PreAuthorize("isAuthenticated()")
    public void salvarOperacao(Operacao operacao) {
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
}
