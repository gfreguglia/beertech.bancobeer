package br.com.beertechtalents.lupulo.pocmq.config;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthProviderService implements AuthenticationProvider {

    ContaService contaService;

    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication auth) {
        String login = auth.getName();
        String senha = auth.getCredentials().toString();

        Optional<Conta> optionalConta = contaService.findByEmail(login);

        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            if (passwordEncoder.matches(senha, conta.getSenha())) {
                return new UsernamePasswordAuthenticationToken(login, senha, conta.getAuthorities());
            } else {
                throw new BadCredentialsException("Este usuário está desativado.");
            }
        }

        throw new UsernameNotFoundException("Login e/ou Senha inválidos.");
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
