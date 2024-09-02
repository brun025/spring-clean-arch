package br.com.api.application;

import br.com.api.domain.User;

public interface CreateUserUseCase {
    public User execute(User pessoa);
}
