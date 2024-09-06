package br.com.api.application.user.update;

public record UpdateUserCommand(
        String id,
        String name,
        String email,
        boolean isActive
) {

    public static UpdateUserCommand with(
            final String anId,
            final String aName,
            final String aEmail,
            final boolean isActive
    ) {
        return new UpdateUserCommand(anId, aName, aEmail, isActive);
    }
}
