package br.com.beertechtalents.lupulo.pocmq.exception;

public class TokenInvalidException extends RuntimeException {

    public TokenInvalidException(String message) {
        super(message);
    }

    public static TokenInvalidException invalidToken(){
        return  new TokenInvalidException("Invalid token");
    }
}
