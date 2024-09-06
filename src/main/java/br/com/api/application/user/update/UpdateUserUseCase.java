package br.com.api.application.user.update;

import br.com.api.application.UseCase;
import br.com.api.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateUserUseCase
        extends UseCase<UpdateUserCommand, Either<Notification, UpdateUserOutput>> {
}
