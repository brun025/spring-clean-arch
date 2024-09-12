package br.com.api.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.api.ControllerTest;
import br.com.api.application.user.create.CreateUserOutput;
import br.com.api.application.user.create.CreateUserUseCase;
import br.com.api.application.user.delete.DeleteUserUseCase;
import br.com.api.application.user.retrieve.get.GetUserByIdUseCase;
import br.com.api.application.user.retrieve.get.UserOutput;
import br.com.api.application.user.retrieve.list.ListUsersUseCase;
import br.com.api.application.user.retrieve.list.UserListOutput;
import br.com.api.application.user.update.UpdateUserOutput;
import br.com.api.application.user.update.UpdateUserUseCase;
import br.com.api.domain.Fixture;
import br.com.api.domain.exceptions.DomainException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserID;
import br.com.api.domain.validation.handler.Notification;
import br.com.api.infrastructure.dtos.CreateUserRequest;
import br.com.api.infrastructure.dtos.UpdateUserRequest;
import br.com.api.domain.validation.Error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = UserAPI.class)
public class UserAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetUserByIdUseCase getUserByIdUseCase;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private ListUsersUseCase listUsersUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateUser_shouldReturnUserId() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Right(CreateUserOutput.from("123")));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/users/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final String expectedName = null;
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenAEmptyName_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = "";
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be empty";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenANullEmail_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final String expectedEmail = null;
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedMessage = "'email' should not be null";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    
    @Test
    public void givenAInvalidEmail_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = "test";
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedMessage = "Invalid E-mail";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenAInvalidPassword_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final String expectedPassword = null;
        final var expectedIsActive = true;
        final var expectedMessage = "'password' should not be null";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenAEmptyPassword_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = "";
        final var expectedIsActive = true;
        final var expectedMessage = "'password' should not be empty";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenAPasswordOfLessThen5Characters_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = "123";
        final var expectedIsActive = true;
        final var expectedMessage = "'password' must be between 5 and 20 characters";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAPasswordLongerThen20Characters_whenCallsCreateUser_thenShouldReturnNotification() throws Exception {
        // given
    	final var expectedName = Fixture.name();
        final var expectedEmail = Fixture.email();
        final var expectedPassword = "1111111111111111111111111";
        final var expectedIsActive = true;
        final var expectedMessage = "'password' must be between 5 and 20 characters";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // when
        final var request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateUser_thenShouldReturnDomainException() throws Exception {
        // given
    	final String expectedName = null;
        final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateUserRequest(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        when(createUserUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));
    }

    @Test
    public void givenAValidId_whenCallsGetUser_shouldReturnUser() throws Exception {
        // given
    	final var expectedName = Fixture.name();
    	final var expectedEmail = Fixture.email();
        final var expectedPassword = Fixture.password();
        final var expectedIsActive = true;

        final var aUser =
                User.newUser(expectedName, expectedEmail, expectedPassword, expectedIsActive);

        final var expectedId = aUser.getId().getValue();

        when(getUserByIdUseCase.execute(any()))
                .thenReturn(UserOutput.from(aUser));

        // when
        final var request = get("/api/v1/users/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aUser.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aUser.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aUser.getDeletedAt())));

        verify(getUserByIdUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetUser_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "User with ID 123 was not found";
        final var expectedId = UserID.from("123");

        when(getUserByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(User.class, expectedId));

        // when
        final var request = get("/api/v1/users/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateUser_shouldReturnUserId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = Fixture.name();
    	final var expectedEmail = Fixture.email();
        final var expectedIsActive = true;

        when(updateUserUseCase.execute(any()))
                .thenReturn(Right(UpdateUserOutput.from(expectedId)));

        final var aCommand =
                new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        // when
        final var request = put("/api/v1/users/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateUser_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = Fixture.name();
    	final var expectedEmail = Fixture.email();
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessage = "'name' should not be null";

        when(updateUserUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var aCommand =
                new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        // when
        final var request = put("/api/v1/users/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }
    
    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateUser_shouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = Fixture.name();
    	final var expectedEmail = Fixture.email();
        final var expectedIsActive = true;

        final var expectedErrorMessage = "User with ID not-found was not found";

        when(updateUserUseCase.execute(any()))
                .thenThrow(NotFoundException.with(User.class, UserID.from(expectedId)));

        final var aCommand =
                new UpdateUserRequest(expectedName, expectedEmail, expectedIsActive);

        // when
        final var request = put("/api/v1/users/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateUserUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedEmail, cmd.email())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteUser_shouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteUserUseCase).execute(any());

        // when
        final var request = delete("/api/v1/users/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteUserUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListUsers_shouldReturnUsers() throws Exception {
        // given
        final var aUser = User.newUser("Carlos", "carlos@gmail.com", "12345", true);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "carlos";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(UserListOutput.from(aUser));

        when(listUsersUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = get("/api/v1/users")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aUser.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aUser.getName())))
                .andExpect(jsonPath("$.items[0].email", equalTo(aUser.getEmail())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aUser.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aUser.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aUser.getDeletedAt())));

        verify(listUsersUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}

