package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.Data;

import java.math.BigInteger;

@Data
public class DadosUsuarioSessaoDTO {
    final String token;
    final String email;
    final String nome;
    final BigInteger cnpj;
    final Conta.PerfilUsuario perfil;
}
