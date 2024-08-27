package br.com.api.infrastructure.gateways;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.api.core.entities.User;
import br.com.api.infrastructure.persistence.UserEntity;

@Component
public class UserEntityMapper {

	private ModelMapper modelMapper;

    public UserEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    public UserEntity toEntity(User user) {
        return modelMapper.map(user, UserEntity.class);
    }

    public User toPessoa(UserEntity entity) {
        return  modelMapper.map(entity, User.class);
    }
}
