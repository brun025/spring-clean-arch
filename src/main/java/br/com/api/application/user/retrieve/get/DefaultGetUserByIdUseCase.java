package br.com.api.application.user.retrieve.get;

import java.util.Objects;
import java.util.function.Supplier;

import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;

public class DefaultGetUserByIdUseCase extends GetUserByIdUseCase {

    private final UserGateway userGateway;

    public DefaultGetUserByIdUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public UserOutput execute(final String anIn) {
        final var anUserID = UserID.from(anIn);

        return this.userGateway.findById(anUserID)
                .map(UserOutput::from)
                .orElseThrow(notFound(anUserID));
    }

    private Supplier<NotFoundException> notFound(final UserID anId) {
        return () -> NotFoundException.with(User.class, anId);
    }
}
