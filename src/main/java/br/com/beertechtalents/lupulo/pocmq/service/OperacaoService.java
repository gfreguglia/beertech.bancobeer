package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    final OperacaoRepository operacaoRepository;

    public void salvarOperacao(Operacao operacao){
        operacaoRepository.save(operacao);
    }

    public BigDecimal buscarSaldo(){
        return operacaoRepository.somaSaldo();
    }

}
