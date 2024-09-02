package br.com.api.application.impl;

import br.com.api.application.DeleteUserUseCase;
import br.com.api.domain.UserGateway;

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
