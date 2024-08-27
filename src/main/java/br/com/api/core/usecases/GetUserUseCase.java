package br.com.api.core.usecases;

import br.com.api.core.entities.User;
import br.com.api.core.exceptions.UserNotFoundException;

public interface GetUserUseCase {

	public User execute(Long id) throws UserNotFoundException;
}
