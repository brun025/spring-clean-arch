package br.com.api.e2e;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import br.com.api.domain.Identifier;
import br.com.api.domain.user.UserID;
import br.com.api.infrastructure.configuration.json.Json;
import br.com.api.infrastructure.dtos.CreateUserRequest;
import br.com.api.infrastructure.dtos.UpdateUserRequest;
import br.com.api.infrastructure.dtos.UserResponse;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    /**
     * User
     */

    default ResultActions deleteAUser(final UserID anId) throws Exception {
        return this.delete("/api/v1/users/", anId);
    }

    default UserID givenAUser(final String aName, final String aEmail, final String aPassword, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateUserRequest(aName, aEmail, aPassword, isActive);
        final var actualId = this.given("/api/v1/users", aRequestBody);
        return UserID.from(actualId);
    }

    default ResultActions listUsers(final int page, final int perPage) throws Exception {
        return listUsers(page, perPage, "", "", "");
    }

    default ResultActions listUsers(final int page, final int perPage, final String search) throws Exception {
        return listUsers(page, perPage, search, "", "");
    }

    default ResultActions listUsers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/api/v1/users", page, perPage, search, sort, direction);
    }

    default UserResponse retrieveAUser(final UserID anId) throws Exception {
        return this.retrieve("/api/v1/users/", anId, UserResponse.class);
    }

    default ResultActions updateAUser(final UserID anId, final UpdateUserRequest aRequest) throws Exception {
        return this.update("/api/v1/users/", anId, aRequest);
    }

    default <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream()
                .map(mapper)
                .toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }
}

