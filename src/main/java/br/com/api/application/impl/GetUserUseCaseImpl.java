package br.com.api.application.impl;

import java.util.Optional;

import br.com.api.application.GetUserUseCase;
import br.com.api.domain.User;
import br.com.api.domain.UserGateway;

public class GetUserUseCaseImpl implements  GetUserUseCase {

    private final UserGateway userGateway;

    public GetUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public Optional<User> execute(Long id) {
        return userGateway.getById(id);
    }
}
