package br.com.api.application.user.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.api.IntegrationTest;
import br.com.api.domain.Fixture;
import br.com.api.domain.exceptions.EmailAlreadyExistsException;
import br.com.api.domain.user.UserGateway;
import br.com.api.infrastructure.persistence.UserRepository;

@IntegrationTest
public class CreateUserUseCaseImplIT {

	@Autowired
    private CreateUserUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserGateway userGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateUser_shouldReturnAUser() {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, userRepository.count());

        final var actualUser =
        		userRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedPassword, actualUser.getPassword());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertNotNull(actualUser.getCreatedAt());
        Assertions.assertNotNull(actualUser.getUpdatedAt());
        Assertions.assertNull(actualUser.getDeletedAt());
    }
    
    @Test
    public void givenACommandWithEmailDuplicated_whenCallsCreateUser_shouldReturnAEmailAlreadyExistsException() {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorMessage = "E-mail already exists, please enter another";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, userRepository.count());

        final var anotherCommand =
                CreateUserCommand.with(Fixture.name(), expectedEmail, Fixture.password(), expectedIsActive);
        
        final var actualException =
                Assertions.assertThrows(EmailAlreadyExistsException.class, () -> useCase.execute(anotherCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
    
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";
        
        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
        .when(userGateway).create(any());

		final var notification = useCase.execute(aCommand).getLeft();
		
		Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
		Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }

}
