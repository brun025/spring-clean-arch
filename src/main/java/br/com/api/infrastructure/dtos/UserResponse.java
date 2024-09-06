package br.com.api.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UserResponse(
		@JsonProperty("id") String id,
	    @JsonProperty("name") String name,
	    @JsonProperty("email") String email,
	    @JsonProperty("password") String password,
	    @JsonProperty("is_active") Boolean active,
	    @JsonProperty("created_at") Instant createdAt,
	    @JsonProperty("updated_at") Instant updatedAt,
	    @JsonProperty("deleted_at") Instant deletedAt
) {
	
}
