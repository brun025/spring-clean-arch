package br.com.api.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException() {
        super("E-mail já existe na base de dados.");
    }
	
	public EmailAlreadyExistsException(String message) {
        super(message);
    }

}
