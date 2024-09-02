package br.com.api.application;

import java.util.Optional;

import br.com.api.domain.User;

public interface GetUserUseCase {

	public Optional<User> execute(Long id);
}
