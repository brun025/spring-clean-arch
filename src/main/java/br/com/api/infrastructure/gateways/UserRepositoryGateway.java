package br.com.api.infrastructure.gateways;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import br.com.api.domain.User;
import br.com.api.domain.UserGateway;
import br.com.api.domain.exceptions.EmailAlreadyExistsException;
import br.com.api.domain.exceptions.UserNotFoundException;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryGateway implements UserGateway {

    private final UserRepository userRepository;
    private final UserEntityMapper entityMapper;

    @Override
    public User createPessoa(User pessoa) {
    	
        UserEntity entity = entityMapper.toEntity(pessoa);
        
        Optional<UserEntity> emailAlreadyExists = userRepository.findByEmail(entity.getEmail());
        if (emailAlreadyExists.isPresent()) {
        	throw new EmailAlreadyExistsException("E-mail já existe na base dados.");
        }
        
        UserEntity newUSer = userRepository.save(entity);
        return entityMapper.toPessoa(newUSer);
    }

    @Override
    public List<User> obtainAllPessoas() {
        return userRepository
                .findAll()
                .stream()
                .map(entityMapper::toPessoa)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<User> getById(Long id) {
    	
    	UserEntity user = userRepository.findById(id)
    			.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
    	
    	return Optional.ofNullable(entityMapper.toPessoa(user));
    }

	@Override
	public User updateUser(User user) {
		
		UserEntity alterUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));
		
		if (user.getEmail() != alterUser.getEmail()) {
			
			Optional<UserEntity> emailAlreadyExists = userRepository.findByEmail(user.getEmail());
			if (emailAlreadyExists.isPresent()) {
				throw new EmailAlreadyExistsException("E-mail já existe na base dados.");
			}			
		}
        
		alterUser.setName(user.getName());
		alterUser.setEmail(user.getEmail());
        return entityMapper.toPessoa(userRepository.save(alterUser));
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.delete(userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado.")));
	}
}
