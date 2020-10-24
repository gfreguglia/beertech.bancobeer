package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.events.EventPublisher;
import br.com.beertechtalents.lupulo.pocmq.events.RequestChangePasswordEvents;
import br.com.beertechtalents.lupulo.pocmq.exception.TokenInvalidException;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.model.TokenTrocarSenha;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.TokenResetarSenhaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContaService {

    final ContaRepository contaRepository;

    final PasswordEncoder passwordEncoder;

    final TokenResetarSenhaRepository tokenResetarSenhaRepository;

    final EventPublisher eventPublisher;

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

    public Optional<Conta> findByCnpj(String cnpj) {
        return contaRepository.findByCnpj(cnpj);
    }

    public void sendRequestTrocarSenha(String email) {

        Optional<Conta> optionalConta = findByEmail(email);

        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            TokenTrocarSenha tokenResetarSenha = new TokenTrocarSenha(conta);

            tokenResetarSenhaRepository.save(tokenResetarSenha);

            eventPublisher.fire(RequestChangePasswordEvents.createRequestChangePasswordEvents(tokenResetarSenha));
        }
    }

    public Optional<Conta> getContabyTokenTrocarSenha(UUID uuid) {
        Optional<TokenTrocarSenha> optionalToken = tokenResetarSenhaRepository.findByUuid(uuid);

        if (optionalToken.isEmpty()) {
            throw TokenInvalidException.invalidToken();
        }

        TokenTrocarSenha token = optionalToken.get();

        if (token.isInvalido() || token.hasExpired()) {
            throw TokenInvalidException.invalidToken();
        }

        return Optional.of(token.getConta());
    }

    @Transactional
    public void trocarSenha(UUID contaUuid, String senha, UUID tokenUuid) {

        Optional<TokenTrocarSenha> optionalToken = tokenResetarSenhaRepository.findByUuid(tokenUuid);

        if (optionalToken.isEmpty()) {
            throw TokenInvalidException.invalidToken();
        }

        TokenTrocarSenha token = optionalToken.get();

        Conta conta = token.getConta();

        if (token.isInvalido() || !conta.getUuid().equals(contaUuid) || token.hasExpired()) {
            throw TokenInvalidException.invalidToken();
        }

        String encode = passwordEncoder.encode(senha);

        conta.setSenha(encode);

        contaRepository.save(conta);

        token.invalidar();

        tokenResetarSenhaRepository.save(token);
    }

}
