package br.com.beertechtalents.lupulo.pocmq.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.beertechtalents.lupulo.pocmq.Utils;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.repository.ContaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

  @InjectMocks
  private JwtUserDetailsService jwtUserDetailsService;

  @Mock
  ContaRepository contaRepository;

  @Test
  void loadUserByUsername() {
    Conta conta = Utils.criarConta();
    when(contaRepository.findByEmail(any(String.class))).thenReturn(Optional.of(conta));
    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(conta.getEmail());
    assertThat(userDetails.getUsername().equals(conta.getEmail()));
  }

  @Test
  void loadUserByUsername_withNoExistentUser() {
    when(contaRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("email_inexistente");
    assertThat(userDetails == null);
  }

}