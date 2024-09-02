package br.com.api.application.impl;

import java.util.List;

import br.com.api.application.GetAllUsersUseCase;
import br.com.api.domain.User;
import br.com.api.domain.UserGateway;

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
