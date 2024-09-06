package br.com.api.application.user.create;

public record CreateUserCommand(
        String name,
        String email,
        String password,
        boolean isActive
) {

    public static CreateUserCommand with(
            final String aName,
            final String aEmail,
            final String aPassword,
            final boolean isActive
    ) {
        return new CreateUserCommand(aName, aEmail, aPassword, isActive);
    }
}
