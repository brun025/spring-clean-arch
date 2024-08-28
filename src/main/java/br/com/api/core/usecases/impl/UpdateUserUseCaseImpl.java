package br.com.api.core.usecases.impl;

import br.com.api.core.entities.User;
import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.UpdateUserUseCase;

public class UpdateUserUseCaseImpl implements  UpdateUserUseCase {

    private final UserGateway userGateway;

    public UpdateUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(User user) {
       return userGateway.updateUser(user);
    }
}