package br.com.api.application.user.retrieve.get;

import java.time.Instant;

import br.com.api.domain.user.User;
import br.com.api.domain.user.UserID;

public record UserOutput(
        UserID id,
        String name,
        String email,
        String password,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static UserOutput from(final User aUser) {
        return new UserOutput(
        		aUser.getId(),
        		aUser.getName(),
        		aUser.getEmail(),
        		aUser.getPassword(),
        		aUser.isActive(),
        		aUser.getCreatedAt(),
        		aUser.getUpdatedAt(),
        		aUser.getDeletedAt()
        );
    }
}
