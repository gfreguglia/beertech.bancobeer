package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContaService {

    ContaRepository contaRepository;

    PasswordEncoder passwordEncoder;

    public Page<Conta> getPageConta(int page, int size) {
        return contaRepository.findAll(PageRequest.of(page, size));
    }

    public Conta novaConta(Conta conta) {
        String encode = passwordEncoder.encode(conta.getSenha());
        conta.setSenha(encode);
        return contaRepository.save(conta);
    }

    public Optional<Conta> getConta(UUID uuid) {
        return contaRepository.findByUuid(uuid);
    }

    public BigDecimal computeSaldo(Conta conta) {
        return conta.getOperacoes()
                .stream()
                .map(operacao -> operacao.getTipo().equals(Operacao.TipoTransacao.SAQUE) ? operacao.getValor().negate() : operacao.getValor())
                .reduce(BigDecimal.valueOf(0.0), BigDecimal::add);
    }

    public Optional<Conta> findByEmail(String email) {
        return contaRepository.findByEmail(email);
    }
}
