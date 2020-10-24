package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.DadosUsuarioSessao;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    ContaRepository contaRepository;

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<Conta> optionalConta = contaRepository.findByEmail(login);

        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            return new DadosUsuarioSessao(login, "", conta.getAuthorities(), conta.getEmail(), conta.getNome(), conta.getCnpj(), conta.getUuid());
        } else {
            return null;
        }

    }
}
