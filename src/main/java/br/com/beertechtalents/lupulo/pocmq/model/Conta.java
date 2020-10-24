package br.com.beertechtalents.lupulo.pocmq.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Validated
public class Conta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Length deve ser 16 pois o default do hibernate é 255 e o UUID usa 16
    @NaturalId
    @Column(unique = true, columnDefinition = "BINARY(16)")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    @Size(min = 14, max = 14)
    private String cnpj;

    @Column(nullable = false)
    private String senha;

    @CreatedDate
    private Timestamp criadoEm;

    @OneToMany(mappedBy = "conta")
    List<Operacao> operacoes = new ArrayList<>();

    public Collection<? extends GrantedAuthority> getAuthorities(){
        GrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(uuid.toString());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

}
