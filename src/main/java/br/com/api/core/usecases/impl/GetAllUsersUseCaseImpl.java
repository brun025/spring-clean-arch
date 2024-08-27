package br.com.api.core.usecases.impl;

import java.util.List;

import br.com.api.core.entities.User;
import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.GetAllUsersUseCase;

public class GetAllUsersUseCaseImpl implements  GetAllUsersUseCase {

    private final UserGateway userGateway;

    public GetAllUsersUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public List<User> execute() {
        return userGateway.obtainAllPessoas();
    }
}
