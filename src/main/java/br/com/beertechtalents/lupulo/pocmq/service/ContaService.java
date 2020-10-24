package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import java.util.Collections;
import java.util.Comparator;
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

    public Optional<Conta> getConta(UUID uuid) {
        return contaRepository.findByUuid(uuid);
    }

    public BigDecimal computeSaldo(Conta conta) {
        BigDecimal saldo = conta.getOperacoes().stream()
            .max(Comparator.comparing(Operacao::getId))
            .map(Operacao::getSaldoAtual)
            .orElse(BigDecimal.ZERO);
        return saldo;
    }

    public Optional<Conta> findByEmail(String email) {
        return contaRepository.findByEmail(email);
    }

    public Optional<Conta> findByCnpj(String cnpj) {
        return contaRepository.findByCnpj(cnpj);
    }

    public Conta salvar(Conta conta) {
        return contaRepository.save(conta);
    }
}
