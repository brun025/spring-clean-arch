package br.com.api.application.impl;

import br.com.api.application.UpdateUserUseCase;
import br.com.api.domain.User;
import br.com.api.domain.UserGateway;

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