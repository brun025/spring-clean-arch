package br.com.api.application.user.retrieve.list;

import java.time.Instant;

import br.com.api.domain.user.User;
import br.com.api.domain.user.UserID;

public record UserListOutput(
		UserID id,
        String name,
        String email,
        String password,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static UserListOutput from(final User aUser) {
        return new UserListOutput(
        		aUser.getId(),
        		aUser.getName(),
                aUser.getEmail(),
                aUser.getPassword(),
                aUser.isActive(),
                aUser.getCreatedAt(),
                aUser.getDeletedAt()
        );
    }
}
