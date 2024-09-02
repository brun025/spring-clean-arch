package br.com.api.infrastructure.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.api.application.CreateUserUseCase;
import br.com.api.application.DeleteUserUseCase;
import br.com.api.application.GetAllUsersUseCase;
import br.com.api.application.GetUserUseCase;
import br.com.api.application.UpdateUserUseCase;
import br.com.api.application.impl.CreateUserUseCaseImpl;
import br.com.api.application.impl.DeleteUserUseCaseImpl;
import br.com.api.application.impl.GetAllUsersUseCaseImpl;
import br.com.api.application.impl.GetUserUseCaseImpl;
import br.com.api.application.impl.UpdateUserUseCaseImpl;
import br.com.api.domain.UserGateway;

@Configuration
public class BeansConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway) {
        return new CreateUserUseCaseImpl(userGateway);
    }

    @Bean
    public GetAllUsersUseCase getAllUsersUseCase(UserGateway userGateway) {
        return new GetAllUsersUseCaseImpl(userGateway);
    }
    
    @Bean
    public GetUserUseCase getUserUseCase(UserGateway userGateway) {
        return new GetUserUseCaseImpl(userGateway);
    }
    
    @Bean
    public UpdateUserUseCase updateUserUseCase(UserGateway userGateway) {
        return new UpdateUserUseCaseImpl(userGateway);
    }
    
    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway) {
        return new DeleteUserUseCaseImpl(userGateway);
    }
    
    @Bean
    public ModelMapper modelMapper () {
        return new ModelMapper();
    }
}
