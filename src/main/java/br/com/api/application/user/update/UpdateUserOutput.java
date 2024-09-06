package br.com.api.application.user.update;

import br.com.api.domain.user.User;

public record UpdateUserOutput(
        String id
) {

    public static UpdateUserOutput from(final String anId) {
        return new UpdateUserOutput(anId);
    }

    public static UpdateUserOutput from(final User aUser) {
        return new UpdateUserOutput(aUser.getId().getValue());
    }
}
