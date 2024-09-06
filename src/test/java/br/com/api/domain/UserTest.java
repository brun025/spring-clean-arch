package br.com.api.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.api.domain.user.User;

public class UserTest extends UnitTest {
	
	@Test
    public void givenAValidParams_whenCallNewUser_thenInstantiateAUser() {
        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var actualCategory =
        		User.newUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedEmail, actualCategory.getEmail());
        Assertions.assertEquals(expectedPassword, actualCategory.getPassword());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
