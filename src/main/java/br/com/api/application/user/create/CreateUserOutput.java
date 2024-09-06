package br.com.api.application.user.create;

import br.com.api.domain.user.User;

public record CreateUserOutput(
        String id
) {

    public static CreateUserOutput from(final String anId) {
        return new CreateUserOutput(anId);
    }

    public static CreateUserOutput from(final User aUser) {
        return new CreateUserOutput(aUser.getId().getValue());
    }
}
