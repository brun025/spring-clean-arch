package br.com.api.application.user.update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.api.IntegrationTest;
import br.com.api.domain.Fixture;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

@IntegrationTest
public class UpdateUserUseCaseImplIT {
	
	@Autowired
    private UpdateUserUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserGateway userGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateUser_shouldReturnUserId() {
        final var aUser =
                User.newUser(Fixture.name(), Fixture.email(), Fixture.password(), true);

        save(aUser);

        final var expectedName = "Alter test";
        final var expectedEmail = "altertest@gmail.com";
        final var expectedIsActive = true;
        final var expectedId = aUser.getId();

        final var aCommand = UpdateUserCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedEmail,
                expectedIsActive
        );

        Assertions.assertEquals(1, userRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualUser =
                userRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertEquals(aUser.getCreatedAt(), actualUser.getCreatedAt());
        Assertions.assertTrue(aUser.getUpdatedAt().isBefore(actualUser.getUpdatedAt()));
        Assertions.assertNull(actualUser.getDeletedAt());
    }
    
    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateUser_shouldReturnInactiveUserId() {
        final var aUser =
                User.newUser(Fixture.name(), Fixture.email(), Fixture.password(), true);

        save(aUser);

        final var expectedName = "Alter test";
        final var expectedEmail = "altertest@gmail.com";
        final var expectedIsActive = false;
        final var expectedId = aUser.getId();

        final var aCommand = UpdateUserCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedEmail,
                expectedIsActive
        );

        Assertions.assertTrue(aUser.isActive());
        Assertions.assertNull(aUser.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualUser =
                userRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertEquals(aUser.getCreatedAt(), actualUser.getCreatedAt());
        Assertions.assertTrue(aUser.getUpdatedAt().isBefore(actualUser.getUpdatedAt()));
        Assertions.assertNotNull(actualUser.getDeletedAt());
    }
    
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    	final var aUser =
                User.newUser(Fixture.name(), Fixture.email(), Fixture.password(), true);

        save(aUser);

        final var expectedName = "Alter test";
        final var expectedEmail = "altertest@gmail.com";
        final var expectedIsActive = true;
        final var expectedId = aUser.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateUserCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedEmail,
                expectedIsActive
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(userGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualUser =
                userRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aUser.getName(), actualUser.getName());
        Assertions.assertEquals(aUser.getEmail(), actualUser.getEmail());
        Assertions.assertEquals(aUser.isActive(), actualUser.isActive());
        Assertions.assertEquals(aUser.getCreatedAt(), actualUser.getCreatedAt());
        Assertions.assertEquals(aUser.getUpdatedAt(), actualUser.getUpdatedAt());
        Assertions.assertEquals(aUser.getDeletedAt(), actualUser.getDeletedAt());
    }
    
    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateUser_shouldReturnNotFoundException() {
        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorMessage = "User with ID 123 was not found";

        final var aCommand = UpdateUserCommand.with(
                expectedId,
                expectedName,
                expectedEmail,
                expectedIsActive
        );

        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

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
