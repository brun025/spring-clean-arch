package br.com.api.application.user.delete;

import br.com.api.IntegrationTest;
import br.com.api.domain.Fixture;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteUserUseCaseIT {

    @Autowired
	private DeleteUserUseCase useCase;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private UserGateway userGateway;

    @Test
    public void givenAValidId_whenCallsDeleteUser_shouldBeOK() {
        final var aUser = User.newUser(Fixture.name(), Fixture.email(), Fixture.password(), true);
        final var expectedId = aUser.getId();

        save(aUser);

        Assertions.assertEquals(1, userRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteUser_shouldBeOK() {
        final var expectedId = UserID.from("123");

        Assertions.assertEquals(0, userRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, userRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var aUser = User.newUser(Fixture.name(), Fixture.email(), Fixture.password(), true);
        final var expectedId = aUser.getId();

        doThrow(new IllegalStateException("Gateway error"))
                .when(userGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(userGateway, times(1)).deleteById(eq(expectedId));
    }

    private void save(final User... aUser) {
        userRepository.saveAllAndFlush(
                Arrays.stream(aUser)
                        .map(UserEntity::from)
                        .toList()
        );
    }
}
