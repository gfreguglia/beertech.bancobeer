package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.Data;

import java.math.BigInteger;

@Data

public class NovaContaDTO {
    String nome;
    String email;
    BigInteger cnpj;
    String senha;
    Conta.PerfilUsuario perfil;
}
