package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.util.*;


@Data
@Document
public class Conta {

    @Id
    private String id;

    // Length deve ser 16 pois o default do hibernate é 255 e o UUID usa 16
    @Indexed(unique = true)
    private UUID uuid = UUID.randomUUID();

    private String nome;

    @Indexed(name = "_email", unique = true)
    private String email;

    @Indexed(unique = true)
    private String cnpj;

    private String senha;

    private BigDecimal saldo = BigDecimal.ZERO;

    @Setter
    private PerfilUsuario perfil;

    @CreatedDate
    private Date criadoEm;

    List<Operacao> operacoes = new ArrayList<>();

    public void depositar(Operacao op) {
        this.saldo = this.saldo.add(op.getValor());
        this.operacoes.add(op);
    }

    public void sacar(Operacao op) {
        this.saldo = this.saldo.subtract(op.getValor());
        this.operacoes.add(op);
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(perfil.toString());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    public enum PerfilUsuario {
        ADMIN,
        USER
    }

}
