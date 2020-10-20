package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransferenciaService {

    ContaService contaService;

    @Transactional
    public void transferir(UUID origem, UUID destino, BigDecimal valor) throws HttpClientErrorException {
        Conta contaOrigem = contaService.getConta(origem).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "Conta origem não encontrada."));

        Operacao op = new Operacao();
        op.setValor(valor);
        op.setTipo(Operacao.TipoTransacao.SAQUE);
        contaOrigem.sacar(op);
        contaService.atualizaConta(contaOrigem);


        Conta contaDestino = contaService.getConta(destino).orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "Conta destino não encontrada."));

        op = new Operacao();
        op.setValor(valor);
        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
        contaDestino.depositar(op);
        contaService.atualizaConta(contaDestino);
    }
}
