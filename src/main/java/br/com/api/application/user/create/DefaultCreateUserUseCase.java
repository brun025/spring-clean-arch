package br.com.api.application.user.create;

import io.vavr.control.Either;

import java.util.Objects;

import br.com.api.domain.exceptions.EmailAlreadyExistsException;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.validation.handler.Notification;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultCreateUserUseCase extends CreateUserUseCase {

    private final UserGateway userGateway;

    public DefaultCreateUserUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Either<Notification, CreateUserOutput> execute(final CreateUserCommand aCommand) {
        final var aName = aCommand.name();
        final var aEmail = aCommand.email();
        final var aPassword = aCommand.password();
        final var isActive = aCommand.isActive();
        
        if(this.userGateway.existsByEmail(aEmail)) {
    		throw new EmailAlreadyExistsException("E-mail already exists, please enter another");
    	}

        final var notification = Notification.create(); 

        final var aUser = User.newUser(aName, aEmail, aPassword, isActive);
        aUser.validate(notification);

        return notification.hasError() ? Left(notification) : create(aUser);
    }

    private Either<Notification, CreateUserOutput> create(final User aUser) {
        return Try(() -> this.userGateway.create(aUser))
                .toEither()
                .bimap(Notification::create, CreateUserOutput::from);
    }
}
