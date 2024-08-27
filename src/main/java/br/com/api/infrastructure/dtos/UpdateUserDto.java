package br.com.api.infrastructure.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDto(
		@Valid
		
        @NotNull(message = "Nome é obrigatório")
		@NotBlank(message = "Nome é obrigatório")
		String name,
		
		@NotNull(message = "E-mail é obrigatório")
		@NotBlank(message = "E-mail é obrigatório")
		@Email(message = "E-mail inválido")
        String email
) {
}
