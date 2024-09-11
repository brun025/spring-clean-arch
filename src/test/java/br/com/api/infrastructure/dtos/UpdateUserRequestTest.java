package br.com.api.infrastructure.dtos;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import br.com.api.JacksonTest;
import br.com.api.domain.Fixture;

@JacksonTest
public class UpdateUserRequestTest {

	@Autowired
    private JacksonTester<UpdateUserRequest> json;

    @Test
    public void testMarshall() throws Exception {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedIsActive = true;

        final var request =
                new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        final var actualJson = this.json.write(request);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.email", expectedEmail)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedIsActive = true;

        final var json = """
                {
                  "name": "%s",
                  "email": "%s",
                  "is_active": %s
                }    
                """.formatted(expectedName, expectedEmail, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("email", expectedEmail)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
