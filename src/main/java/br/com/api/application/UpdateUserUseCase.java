package br.com.api.application;

import br.com.api.domain.User;

public interface UpdateUserUseCase {
	public User execute(User pessoa);
}
