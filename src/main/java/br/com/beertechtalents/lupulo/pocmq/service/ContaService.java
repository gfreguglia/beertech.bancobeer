package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import br.com.beertechtalents.lupulo.pocmq.model.TokenResetarSenha;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import br.com.beertechtalents.lupulo.pocmq.repository.TokenResetarSenhaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContaService {

    ContaRepository contaRepository;

    PasswordEncoder passwordEncoder;

    TokenResetarSenhaRepository tokenResetarSenhaRepository;

    SendMailAdapter sendMailAdapter;

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

    public void sendRequestResetarSenha(String email) {

        Optional<Conta> optionalConta = findByEmail(email);

        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            TokenResetarSenha tokenResetarSenha = new TokenResetarSenha(conta);

            tokenResetarSenhaRepository.save(tokenResetarSenha);

            Mail mail = new Mail("", "Requisição de mudança de senha", conta.getEmail());
            sendMailAdapter.sendMail(mail);
        }
    }

    public Optional<Conta> getContabyTokenResetarSenha(UUID tokenId) {
        Optional<TokenResetarSenha> optionalToken = tokenResetarSenhaRepository.findById(tokenId);

        if (optionalToken.isEmpty()) {
            return Optional.empty();
        }

        TokenResetarSenha token = optionalToken.get();

        if (token.hasExpired()) {
            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Token expirado");
        }

        return Optional.of(token.getConta());
    }
}
