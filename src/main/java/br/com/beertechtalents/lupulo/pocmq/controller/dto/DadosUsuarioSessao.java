package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class DadosUsuarioSessao extends User {
    final String email;
    final String nome;
    final String cnpj;
    final Conta.PerfilUsuario perfil;

    public DadosUsuarioSessao(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String nome, String cnpj, Conta.PerfilUsuario perfil) {
        super(username, password, authorities);
        this.email = email;
        this.nome = nome;
        this.cnpj = cnpj;
        this.perfil = perfil;
    }
}
