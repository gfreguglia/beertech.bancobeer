package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            return new User(login,"",conta.getAuthorities());
        } else {
            return null;
        }

    }
}
