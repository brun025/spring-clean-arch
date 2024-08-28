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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "UserEndpoint") 
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

    @Operation(summary = "Realiza o cadastro de usuários", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de usuário realizado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar o cadastro de usuário"),
    })
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public User createPessoa(@RequestBody @Valid CreateUserDto userDTO) {
        User newUser = createUserUseCase.execute(pessoaDtoMapper.toEntity(userDTO));
        return newUser;
    }

    @Operation(summary = "Listagem de todos os usuários", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados"),
    })
    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<User> obtainAll() {
        return getAllUsersUseCase.execute()
        		.stream()
                .collect(Collectors.toList());
    }
    
    @Operation(summary = "Busca um usuário", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados"),
    })
    @GetMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> getById(@PathVariable Long id) {
    	Optional<User> user = getUserUseCase.execute(id);
        return user;
    }
    
    @Operation(summary = "Atualiza um usuário", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização de usuário realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar a atualização de usuário"),
    })
    @PutMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDto userDTO) {
        User user = updateUserUseCase.execute(pessoaDtoMapper.toEntityUpdate(id, userDTO));
        return user;
    }
    
    @Operation(summary = "Remove um usuário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "2004", description = "Remoção realizada com sucesso"),
            @ApiResponse(responseCode = "422", description = "Dados de requisição inválida"),
            @ApiResponse(responseCode = "400", description = "Parametros inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro ao realizar busca dos dados"),
    })
    @DeleteMapping(produces = "application/json", value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
    }
}
