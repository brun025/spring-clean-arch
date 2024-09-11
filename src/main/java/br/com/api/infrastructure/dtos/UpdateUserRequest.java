package br.com.api.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateUserRequest(
		@JsonProperty("name") String name,
		@JsonProperty("email") String email,
		@JsonProperty("is_active") Boolean active
) {
}
