package br.com.api.application.user.update;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

import java.util.Objects;
import java.util.function.Supplier;

import br.com.api.domain.exceptions.DomainException;
import br.com.api.domain.exceptions.EmailAlreadyExistsException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;
import br.com.api.domain.validation.handler.Notification;
import io.vavr.control.Either;

public class DefaultUpdateUserUseCase extends UpdateUserUseCase {

    private final UserGateway userGateway;

    public DefaultUpdateUserUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Either<Notification, UpdateUserOutput> execute(final UpdateUserCommand aCommand) {
        final var anId = UserID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aEmail = aCommand.email();
        final var isActive = aCommand.isActive();

        final var aUser = this.userGateway.findById(anId)
                .orElseThrow(notFound(anId));
        
        if (!aEmail.equals(aUser.getEmail())) {
        	if(this.userGateway.existsByEmail(aEmail)) {
        		throw new EmailAlreadyExistsException("E-mail already exists, please enter another");
        	}       	
        }

        final var notification = Notification.create();
        aUser
                .update(aName, aEmail, isActive)
                .validate(notification);

        return notification.hasError() ? Left(notification) : update(aUser);
    }

    private Either<Notification, UpdateUserOutput> update(final User aUser) {
        return Try(() -> this.userGateway.update(aUser))
                .toEither()
                .bimap(Notification::create, UpdateUserOutput::from);
    }

    private Supplier<DomainException> notFound(final UserID anId) {
        return () -> NotFoundException.with(User.class, anId);
    }
}

