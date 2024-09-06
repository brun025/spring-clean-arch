package br.com.api.application.user.create;

import br.com.api.application.UseCase;
import br.com.api.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateUserUseCase
        extends UseCase<CreateUserCommand, Either<Notification, CreateUserOutput>> {
}
