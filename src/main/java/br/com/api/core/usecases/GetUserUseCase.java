package br.com.api.core.usecases;

import java.util.Optional;

import br.com.api.core.entities.User;

public interface GetUserUseCase {

	public Optional<User> execute(Long id);
}
