package br.com.beertechtalents.lupulo.pocmq.controller.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class DadosUsuarioSessao extends User {
    final String email;
    final String nome;
    final String cnpj;
    final String uuid;

    public DadosUsuarioSessao(String username, String password, Collection<? extends GrantedAuthority> authorities, String email, String nome, String cnpj, UUID uuid) {
        super(username, password, authorities);
        this.email = email;
        this.nome = nome;
        this.cnpj = cnpj;
        this.uuid = uuid.toString();
    }
}
