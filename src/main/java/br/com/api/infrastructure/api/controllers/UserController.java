package br.com.api.infrastructure.api.controllers;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.application.user.create.CreateUserCommand;
import br.com.api.application.user.create.CreateUserOutput;
import br.com.api.application.user.create.CreateUserUseCase;
import br.com.api.application.user.delete.DeleteUserUseCase;
import br.com.api.application.user.retrieve.get.GetUserByIdUseCase;
import br.com.api.application.user.retrieve.list.ListUsersUseCase;
import br.com.api.application.user.update.UpdateUserCommand;
import br.com.api.application.user.update.UpdateUserOutput;
import br.com.api.application.user.update.UpdateUserUseCase;
import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.pagination.SearchQuery;
import br.com.api.domain.validation.handler.Notification;
import br.com.api.infrastructure.api.UserAPI;
import br.com.api.infrastructure.dtos.CreateUserRequest;
import br.com.api.infrastructure.dtos.UpdateUserRequest;
import br.com.api.infrastructure.dtos.UserListResponse;
import br.com.api.infrastructure.dtos.UserResponse;
import br.com.api.infrastructure.presenters.UserApiPresenter;

@RestController
public class UserController implements UserAPI {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listCategoriesUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(
            final CreateUserUseCase createUserUseCase,
            final ListUsersUseCase listCategoriesUseCase,
            final GetUserByIdUseCase getUserByIdUseCase,
            final UpdateUserUseCase updateUserUseCase,
            final DeleteUserUseCase deleteUserUseCase
    ) {
        this.createUserUseCase = Objects.requireNonNull(createUserUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
        this.getUserByIdUseCase = Objects.requireNonNull(getUserByIdUseCase);
        this.updateUserUseCase = Objects.requireNonNull(updateUserUseCase);
        this.deleteUserUseCase = Objects.requireNonNull(deleteUserUseCase);
    }
    
    @Override
    public ResponseEntity<?> createUser(final CreateUserRequest input) {
        
        final var aCommand = CreateUserCommand.with(
                input.name(),
                input.email(),
                input.password(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateUserOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/api/v1/users/" + output.id())).body(output);

        return this.createUserUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<UserListResponse> listUsers(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(UserApiPresenter::present);
    }
    
    @Override
    public UserResponse getById(final String id) {
        return UserApiPresenter.present(this.getUserByIdUseCase.execute(id));
    }
    
    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateUserRequest input) {
        final var aCommand = UpdateUserCommand.with(
                id,
                input.name(),
                input.email(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateUserOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return this.updateUserUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteUserUseCase.execute(anId);
    }
}

