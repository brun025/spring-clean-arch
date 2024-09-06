package br.com.api.infrastructure.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateUserRequest(
		@Valid
		
        @NotNull(message = "Nome não deve ser nulo")
		@NotBlank(message = "Nome é obrigatório")
		@JsonProperty("name") String name,
		
		@NotNull(message = "E-mail não deve ser nulo")
		@NotBlank(message = "E-mail é obrigatório")
		@Email(message = "E-mail inválido")
		@JsonProperty("email") String email,
        
        @NotNull(message = "Senha não deve ser nula")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 5, max = 20, message = "Senha deve conter de 5 à 20 caracteres")
		@JsonProperty("password") String password,
        
        @JsonProperty("is_active") Boolean active
) {
}
