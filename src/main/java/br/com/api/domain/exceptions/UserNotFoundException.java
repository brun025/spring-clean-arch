package br.com.api.domain.exceptions;

import java.util.Collections;

import br.com.api.domain.AggregateRoot;
import br.com.api.domain.Identifier;

public class UserNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
        super("Usuário não encontrado.");
    }
	
	public UserNotFoundException(String message) {
        super(message);
    }
	
	public static UserNotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue()
        );
        return new UserNotFoundException(anError);
    }

}
