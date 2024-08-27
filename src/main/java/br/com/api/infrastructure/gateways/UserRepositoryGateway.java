package br.com.api.infrastructure.gateways;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import br.com.api.core.entities.User;
import br.com.api.core.exceptions.UserNotFoundException;
import br.com.api.core.gateways.UserGateway;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryGateway implements UserGateway {

    private final UserRepository userRepository;
    private final UserEntityMapper entityMapper;

    @Override
    public User createPessoa(User pessoa) {
        UserEntity entity = entityMapper.toEntity(pessoa);
        UserEntity novaPessoa = userRepository.save(entity);
        return entityMapper.toPessoa(novaPessoa);
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
    public User getById(Long id) throws UserNotFoundException {
    	
    	UserEntity user = userRepository.findById(id)
    			.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID " + id));
    	
    	return entityMapper.toPessoa(user);
    }

	@Override
	public User updateUser(User user) throws UserNotFoundException {
		
		UserEntity alterUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID " + user.getId()));
		
		alterUser.setName(user.getName());
		alterUser.setEmail(user.getEmail());
        return entityMapper.toPessoa(userRepository.save(alterUser));
	}

	@Override
	public void deleteUser(Long id) throws UserNotFoundException {
		UserEntity deleUser = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID " + id));
		userRepository.delete(deleUser);
	}
}
