package br.com.beertechtalents.lupulo.pocmq.exception;

public class TokenInvalidException extends RuntimeException {

    private TokenInvalidException(String message) {
        super(message);
    }

    public static TokenInvalidException invalidToken(){
        return  new TokenInvalidException("Invalid token");
    }
}
