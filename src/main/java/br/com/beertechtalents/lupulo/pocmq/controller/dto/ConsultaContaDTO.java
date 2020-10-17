package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConsultaContaDTO {
    final UUID uuid;
    final String nome;
    final Timestamp criadoEm;
    final Conta.PerfilUsuario perfil;
    final String email;
    final BigInteger cnpj;
}
