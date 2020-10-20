package br.com.beertechtalents.lupulo.pocmq.exception;

public class BusinessException extends RuntimeException {

    private BusinessException(String message) {
        super(message);
    }

    public static BusinessException insufficientFund(){
     return  new BusinessException("Insufficient funds");
 }
}
