package br.com.api.application.user.delete;

import java.util.Objects;

import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;

public class DefaultDeleteUserUseCase extends DeleteUserUseCase {

    private final UserGateway userGateway;

    public DefaultDeleteUserUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.userGateway.deleteById(UserID.from(anIn));
    }
}
