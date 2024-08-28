package br.com.api.core.usecases;

import br.com.api.core.entities.User;

public interface UpdateUserUseCase {
	public User execute(User pessoa);
}
