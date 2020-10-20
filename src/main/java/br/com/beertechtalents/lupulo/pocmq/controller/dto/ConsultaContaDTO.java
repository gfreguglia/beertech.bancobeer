package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConsultaContaDTO {
    final UUID uuid;
    final String nome;
    final Date criadoEm;
    final BigDecimal saldo;
    final Conta.PerfilUsuario perfil;
    final String email;
    final String cnpj;
}
