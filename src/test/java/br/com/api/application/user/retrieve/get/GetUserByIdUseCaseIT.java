package br.com.api.application.user.retrieve.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.api.IntegrationTest;
import br.com.api.domain.Fixture;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetUserByIdUseCaseIT {

    @Autowired
    private GetUserByIdUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserGateway userGateway;

    @Test
    public void givenAValidId_whenCallsGetUser_shouldReturnUser() {
        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var aUser =
        		User.newUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var expectedId = aUser.getId();

        save(aUser);

        final var actualUser = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualUser.id());
        Assertions.assertEquals(expectedName, actualUser.name());
        Assertions.assertEquals(expectedEmail, actualUser.email());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertEquals(aUser.getCreatedAt(), actualUser.createdAt());
        Assertions.assertEquals(aUser.getUpdatedAt(), actualUser.updatedAt());
        Assertions.assertEquals(aUser.getDeletedAt(), actualUser.deletedAt());
    }

    @Test
    public void givenAInvalidId_whenCallsGetUser_shouldReturnNotFound() {
        final var expectedErrorMessage = "User with ID 123 was not found";
        final var expectedId = UserID.from("123");

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = UserID.from("123");

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(userGateway).findById(eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final User... aUser) {
        userRepository.saveAllAndFlush(
                Arrays.stream(aUser)
                        .map(UserEntity::from)
                        .toList()
        );
    }
}

