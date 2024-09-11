package br.com.api.infrastructure.dtos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import br.com.api.JacksonTest;
import br.com.api.domain.Fixture;

@JacksonTest
public class CreateUserRequestTest {

	@Autowired
    private JacksonTester<CreateUserRequest> json;

    @Test
    public void testMarshall() throws Exception {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var request =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.email", expectedEmail)
                .hasJsonPathValue("$.password", expectedPassword)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var json = """
                {
                  "name": "%s",
                  "email": "%s",
                  "password": "%s",
                  "is_active": %s
                }    
                """.formatted(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("email", expectedEmail)
                .hasFieldOrPropertyWithValue("password", expectedPassword)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
