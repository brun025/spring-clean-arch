package br.com.api.infrastructure.dtos;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserListResponse(
		@JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("password") String password,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
		) {

}
