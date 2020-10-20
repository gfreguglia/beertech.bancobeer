package br.com.beertechtalents.lupulo.pocmq.controller.exception;

import java.util.ArrayList;

public class BusinessValidationException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessValidationException(String... details) {
        super(BusinessValidationException.generateMessage(details));
    }

    public BusinessValidationException(ArrayList<String> details) {
        super(BusinessValidationException.generateMessage(details));
    }

    private static String generateMessage(String... searchParams) {
        return String.join(".\n", searchParams);
    }

    private static String generateMessage(ArrayList<String> searchParams) {
        return String.join(".\n", searchParams);
    }
}
