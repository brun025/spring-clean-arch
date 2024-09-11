package br.com.api.domain.exceptions;

public class NoStacktraceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public NoStacktraceException(final String message) {
        this(message, null);
    }

    public NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
