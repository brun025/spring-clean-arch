package br.com.api.infrastructure.configuration.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.api.application.user.create.CreateUserUseCase;
import br.com.api.application.user.create.DefaultCreateUserUseCase;
import br.com.api.application.user.delete.DefaultDeleteUserUseCase;
import br.com.api.application.user.delete.DeleteUserUseCase;
import br.com.api.application.user.retrieve.get.DefaultGetUserByIdUseCase;
import br.com.api.application.user.retrieve.get.GetUserByIdUseCase;
import br.com.api.application.user.retrieve.list.DefaultListUsersUseCase;
import br.com.api.application.user.retrieve.list.ListUsersUseCase;
import br.com.api.application.user.update.DefaultUpdateUserUseCase;
import br.com.api.application.user.update.UpdateUserUseCase;
import br.com.api.domain.user.UserGateway;

@Configuration
public class UserUseCaseConfig {

    private final UserGateway userGateway;

    public UserUseCaseConfig(final UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Bean
    public CreateUserUseCase createUserUseCase() {
        return new DefaultCreateUserUseCase(userGateway);
    }
    
    @Bean
    public ListUsersUseCase listUsersUseCase() {
        return new DefaultListUsersUseCase(userGateway);
    }
    
    @Bean
    public GetUserByIdUseCase getUserByIdUseCase() {
        return new DefaultGetUserByIdUseCase(userGateway);
    }
    
    @Bean
    public UpdateUserUseCase updateUserUseCase() {
        return new DefaultUpdateUserUseCase(userGateway);
    }
    
    @Bean
    public DeleteUserUseCase deleteUserUseCase() {
        return new DefaultDeleteUserUseCase(userGateway);
    }
}

