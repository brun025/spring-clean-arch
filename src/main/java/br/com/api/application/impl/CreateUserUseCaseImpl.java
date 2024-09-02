package br.com.api.application.impl;

import br.com.api.application.CreateUserUseCase;
import br.com.api.domain.User;
import br.com.api.domain.UserGateway;

public class CreateUserUseCaseImpl implements  CreateUserUseCase {

    private final UserGateway userGateway;

    public CreateUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(User user) {
       return userGateway.createPessoa(user);
    }
}
