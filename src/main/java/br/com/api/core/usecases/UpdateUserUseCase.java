package br.com.api.core.usecases;

import br.com.api.core.entities.User;
import br.com.api.core.exceptions.UserNotFoundException;

public interface UpdateUserUseCase {
	public User execute(User pessoa) throws UserNotFoundException;
}
