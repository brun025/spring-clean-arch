package br.com.api.core.usecases.impl;

import br.com.api.core.entities.User;
import br.com.api.core.exceptions.UserNotFoundException;
import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.GetUserUseCase;

public class GetUserUseCaseImpl implements  GetUserUseCase {

    private final UserGateway userGateway;

    public GetUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(Long id) throws UserNotFoundException {
        return userGateway.getById(id);
    }
}
