package br.com.api.infrastructure.controllers;

import org.springframework.stereotype.Component;

import br.com.api.domain.User;
import br.com.api.infrastructure.dtos.CreateUserDto;
import br.com.api.infrastructure.dtos.UpdateUserDto;

@Component
public class UserDtoMapper {
    
    public User toEntity(CreateUserDto user) {
        return new User(null, user.name(), user.email(), user.password());
    }
    
    public User toEntityUpdate(Long id, UpdateUserDto user) {
    	return new User(id, user.name(), user.email(), null);
    }
}
