package br.com.api.core.usecases.impl;

import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.DeleteUserUseCase;

public class DeleteUserUseCaseImpl  implements  DeleteUserUseCase {

    private final UserGateway userGateway;

    public DeleteUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void execute(Long id) {
        userGateway.deleteUser(id);
    }
}
