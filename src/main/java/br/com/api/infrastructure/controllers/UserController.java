package br.com.api.infrastructure.controllers;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import br.com.api.core.entities.User;
import br.com.api.core.usecases.CreateUserUseCase;
import br.com.api.core.usecases.DeleteUserUseCase;
import br.com.api.core.usecases.GetAllUsersUseCase;
import br.com.api.core.usecases.GetUserUseCase;
import br.com.api.core.usecases.UpdateUserUseCase;
import br.com.api.infrastructure.dtos.CreateUserDto;
import br.com.api.infrastructure.dtos.UpdateUserDto;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetAllUsersUseCase getAllUsersUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserDtoMapper pessoaDtoMapper;

    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User createPessoa(@RequestBody @Valid CreateUserDto userDTO) {
        User newUser = createUserUseCase.execute(pessoaDtoMapper.toEntity(userDTO));
        return newUser;
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<User> obtainAll() {
        return getAllUsersUseCase.execute()
        		.stream()
                .collect(Collectors.toList());
    }
    
    @GetMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getById(@PathVariable Long id) {
    	Optional<User> user = getUserUseCase.execute(id);
        return user;
    }
    
    @PutMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto userDTO) {
        User user = updateUserUseCase.execute(pessoaDtoMapper.toEntityUpdate(id, userDTO));
        return user;
    }
    
    @DeleteMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
    }
}
