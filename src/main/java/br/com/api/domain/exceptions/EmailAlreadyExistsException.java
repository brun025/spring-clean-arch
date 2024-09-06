package br.com.api.domain.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public EmailAlreadyExistsException(final String aMessage) {
        super(aMessage);
    }
}
