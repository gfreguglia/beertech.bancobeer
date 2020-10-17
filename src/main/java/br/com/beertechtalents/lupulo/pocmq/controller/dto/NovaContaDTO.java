package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.Data;

@Data

public class NovaContaDTO {
    String nome;
    String email;
    String cnpj;
    String senha;
    Conta.PerfilUsuario perfil;
}
