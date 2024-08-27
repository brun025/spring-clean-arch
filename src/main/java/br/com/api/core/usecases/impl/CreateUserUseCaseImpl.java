package br.com.api.core.usecases.impl;

import br.com.api.core.entities.User;
import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.CreateUserUseCase;

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
