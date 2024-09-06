package br.com.api.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import br.com.api.application.user.create.CreateUserCommand;
import br.com.api.application.user.create.CreateUserUseCase;
import br.com.api.domain.Fixture;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.infrastructure.IntegrationTest;
import br.com.api.infrastructure.persistence.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

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

        final var actualCategory =
        		userRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedEmail, actualCategory.getEmail());
        Assertions.assertEquals(expectedPassword, actualCategory.getPassword());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
