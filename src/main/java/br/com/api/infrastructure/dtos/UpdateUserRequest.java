package br.com.api.infrastructure.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
		@Valid
		
        @NotNull(message = "Nome não deve ser nulo")
		@NotBlank(message = "Nome é obrigatório")
		@JsonProperty("name") String name,
		
		@NotNull(message = "E-mail não deve ser nulo")
		@NotBlank(message = "E-mail é obrigatório")
		@Email(message = "E-mail inválido")
		@JsonProperty("email") String email,
		
		@JsonProperty("is_active") Boolean active
) {
}
