package br.com.api.e2e.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.com.api.E2ETest;
import br.com.api.domain.Fixture;
import br.com.api.domain.user.UserID;
import br.com.api.e2e.MockDsl;
import br.com.api.infrastructure.dtos.UpdateUserRequest;
import br.com.api.infrastructure.persistence.UserRepository;

@E2ETest
@Testcontainers
public class UserE2ETest implements MockDsl {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("api-user")
            .withUsername("postgres")
            .withPassword("postgres");
    
    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry registry) {

      // JDBC DataSource Example
      registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
      registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
      registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
    }

    
    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void shouldBeAbleToCreateANewUserWithValidValues() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var actualId = givenAUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualUser = userRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertNotNull(actualUser.getCreatedAt());
        Assertions.assertNotNull(actualUser.getUpdatedAt());
        Assertions.assertNull(actualUser.getDeletedAt());
    }
    
    @Test
    public void shouldBeAbleToNavigateToAllUsers() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        givenAUser("Ana", "ana@gmail.com", "12345", true);
        givenAUser("Bia", "bia@gmail.com", "12345", true);
        givenAUser("Carlos", "carlos@gmail.com", "12345", true);

        listUsers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Ana")));

        listUsers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Bia")));

        listUsers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Carlos")));

        listUsers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void shouldBeAbleToSearchBetweenAllUsers() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        givenAUser("Ana", "ana@gmail.com", "12345", true);
        givenAUser("Bia", "bia@gmail.com", "12345", true);
        givenAUser("Carlos", "carlos@gmail.com", "12345", true);


        listUsers(0, 1, "an")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Ana")));
    }

    @Test
    public void shouldBeAbleToSortAllUsersByEmailDesc() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        givenAUser("Ana", "ana@gmail.com", "12345", true);
        givenAUser("Bia", "bia@gmail.com", "12345", true);
        givenAUser("Carlos", "carlos@gmail.com", "12345", true);

        listUsers(0, 3, "", "email", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Carlos")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Bia")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Ana")));
    }

    @Test
    public void shouldBeAbleToGetAUserByItsIdentifier() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var actualId = givenAUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var actualUser = retrieveAUser(actualId);

        Assertions.assertEquals(expectedName, actualUser.name());
        Assertions.assertEquals(expectedEmail, actualUser.email());
        Assertions.assertEquals(expectedIsActive, actualUser.active());
        Assertions.assertNotNull(actualUser.createdAt());
        Assertions.assertNotNull(actualUser.updatedAt());
        Assertions.assertNull(actualUser.deletedAt());
    }

    @Test
    public void shouldBeAbleToSeeATreatedErrorByGettingANotFoundUser() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var aRequest = get("/api/v1/users/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("User with ID 123 was not found")));
    }

    @Test
    public void shouldBeAbleToUpdateAUserByItsIdentifier() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var actualId = givenAUser(Fixture.name(), Fixture.email(), Fixture.password(), true);

        final var expectedName = "User1";
        final var expectedEmail = "user1@gmail.com";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        updateAUser(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualUser = userRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertNotNull(actualUser.getCreatedAt());
        Assertions.assertNotNull(actualUser.getUpdatedAt());
        Assertions.assertNull(actualUser.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToInactivateAUserByItsIdentifier() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = false;

        final var actualId = givenAUser(expectedName, expectedEmail, expectedPassword, true);

        final var aRequestBody = new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        updateAUser(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualUser = userRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertNotNull(actualUser.getCreatedAt());
        Assertions.assertNotNull(actualUser.getUpdatedAt());
        Assertions.assertNotNull(actualUser.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToActivateAUserByItsIdentifier() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var actualId = givenAUser(expectedName, expectedEmail, expectedPassword, false);

        final var aRequestBody = new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        updateAUser(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualUser = userRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualUser.getName());
        Assertions.assertEquals(expectedEmail, actualUser.getEmail());
        Assertions.assertEquals(expectedIsActive, actualUser.isActive());
        Assertions.assertNotNull(actualUser.getCreatedAt());
        Assertions.assertNotNull(actualUser.getUpdatedAt());
        Assertions.assertNull(actualUser.getDeletedAt());
    }

    @Test
    public void shouldBeAbleToDeleteAUserByItsIdentifier() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var actualId = givenAUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        deleteAUser(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.userRepository.existsById(actualId.getValue()));
    }

    @Test
    public void shouldNotSeeAnErrorByDeletingANotExistentUser() throws Exception {
        Assertions.assertTrue(POSTGRESQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, userRepository.count());

        deleteAUser(UserID.from("12313"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, userRepository.count());
    }
}

