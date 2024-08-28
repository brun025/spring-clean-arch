package br.com.api.infrastructure.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
		@Valid
		
        @NotNull(message = "Nome não deve ser nulo")
		@NotBlank(message = "Nome é obrigatório")
		String name,
		
		@NotNull(message = "E-mail não deve ser nulo")
		@NotBlank(message = "E-mail é obrigatório")
		@Email(message = "E-mail inválido")
        String email,
        
        @NotNull(message = "Senha não deve ser nula")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 5, max = 20, message = "Senha deve conter de 5 à 20 caracteres")
        String password
) {
}
