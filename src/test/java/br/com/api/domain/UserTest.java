package br.com.api.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest extends UnitTest {
	
	@Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateAUser() {
        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();

        final var actualCategory =
                new User(null, expectedName, expectedEmail, expectedPassword);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedEmail, actualCategory.getEmail());
        Assertions.assertEquals(expectedPassword, actualCategory.getPassword());
    }
}
