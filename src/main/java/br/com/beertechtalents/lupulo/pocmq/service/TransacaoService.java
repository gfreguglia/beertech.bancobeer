package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Transacao;
import br.com.beertechtalents.lupulo.pocmq.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    final TransacaoRepository transacaoRepository;

    public void salvarTransacao(Transacao transacao){
        transacaoRepository.save(transacao);
    }

    public BigDecimal buscarSaldo(){
        return transacaoRepository.somaSaldo();
    }

}
