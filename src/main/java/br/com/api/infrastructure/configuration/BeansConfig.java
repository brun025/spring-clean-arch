package br.com.api.infrastructure.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.api.core.gateways.UserGateway;
import br.com.api.core.usecases.CreateUserUseCase;
import br.com.api.core.usecases.DeleteUserUseCase;
import br.com.api.core.usecases.GetAllUsersUseCase;
import br.com.api.core.usecases.GetUserUseCase;
import br.com.api.core.usecases.UpdateUserUseCase;
import br.com.api.core.usecases.impl.CreateUserUseCaseImpl;
import br.com.api.core.usecases.impl.DeleteUserUseCaseImpl;
import br.com.api.core.usecases.impl.GetAllUsersUseCaseImpl;
import br.com.api.core.usecases.impl.GetUserUseCaseImpl;
import br.com.api.core.usecases.impl.UpdateUserUseCaseImpl;

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
