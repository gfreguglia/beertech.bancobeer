package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository operacaoRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    public void salvarOperacao(Operacao operacao) {
        operacaoRepository.save(operacao);
    }

}
