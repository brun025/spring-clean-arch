package br.com.api.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserRequest(
		@JsonProperty("name") String name,
		@JsonProperty("email") String email,
		@JsonProperty("password") String password,
        @JsonProperty("is_active") Boolean active
) {
}
