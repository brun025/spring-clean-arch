package br.com.api.core.usecases;

import br.com.api.core.exceptions.UserNotFoundException;

public interface DeleteUserUseCase {
	public void execute(Long id) throws UserNotFoundException;
}
