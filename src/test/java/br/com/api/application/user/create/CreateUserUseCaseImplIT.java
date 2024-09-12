package br.com.api.application.user.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.api.IntegrationTest;
import br.com.api.domain.Fixture;
import br.com.api.domain.exceptions.DomainException;
import br.com.api.domain.exceptions.EmailAlreadyExistsException;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.validation.Error;
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
    public void givenACommandWithInvalidName_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final String expectedName = null;
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
    }
    
    @Test
    public void givenACommandWithEmptyName_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final var expectedName = "";
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
    }
    
    @Test
    public void givenACommandWithInvalidEmail_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final var expectedName = Fixture.name();
        final var expectedEmail = "test";
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Invalid E-mail";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
    }
    
    @Test
    public void givenACommandWithNullEmail_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final var expectedName = Fixture.name();
        final String expectedEmail = null;
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'email' should not be null";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
    }
    
    @Test
    public void givenACommandWithEmptyPassword_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = "";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'password' should not be empty";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
    }
    
    @Test
    public void givenACommandWithInvalidPassword_whenCallsCreateUser_shouldReturnADomainException() {
    	
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final String expectedPassword = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'password' should not be null";

        Assertions.assertEquals(0, userRepository.count());

        final var aCommand =
                CreateUserCommand.with(expectedName, expectedEmail, expectedPassword, expectedIsActive);
        
        when(useCase.execute(aCommand))
        .thenThrow(DomainException.with(new Error(expectedErrorMessage)));
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
