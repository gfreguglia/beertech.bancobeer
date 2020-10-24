package br.com.beertechtalents.lupulo.pocmq.controller.exception;

import java.util.List;

public class BusinessValidationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BusinessValidationException(String... details) {
        super(BusinessValidationException.generateMessage(details));
    }

    public BusinessValidationException(List<String> details) {
        super(BusinessValidationException.generateMessage(details));
    }

    private static String generateMessage(String... searchParams) {
        return String.join(".\n", searchParams);
    }

    private static String generateMessage(List<String> searchParams) {
        return String.join(".\n", searchParams);
    }
}
