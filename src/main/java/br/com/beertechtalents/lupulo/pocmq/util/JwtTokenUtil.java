package br.com.beertechtalents.lupulo.pocmq.util;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.DadosUsuarioSessao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5L * 60L * 60L;

    @Value("${jwt.secret:secret}")
    private String secret;

    //retorna o username do token jwt
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retorna expiration date do token jwt
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    //para retornar qualquer informação do token nos iremos precisar da secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //gera token para user
    public String generateToken(UserDetails userDetails) {
        if(userDetails instanceof DadosUsuarioSessao) {
            DadosUsuarioSessao dadosUsuarioSessao = (DadosUsuarioSessao) userDetails;

            Map<String, Object> claims = new HashMap<>();
            claims.put("email", dadosUsuarioSessao.getEmail());
            claims.put("nome", dadosUsuarioSessao.getNome());
            claims.put("cnpj", dadosUsuarioSessao.getCnpj());
            claims.put("uuid", dadosUsuarioSessao.getUuid());
            return doGenerateToken(claims, dadosUsuarioSessao.getUsername());
        } else {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Cria o token e devine tempo de expiração pra ele
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //valida o token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}