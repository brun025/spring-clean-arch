package br.com.api.core.gateways;

import java.util.List;

import br.com.api.core.entities.User;
import br.com.api.core.exceptions.UserNotFoundException;

public interface UserGateway {
	
    User createPessoa(User user);
    List<User> obtainAllPessoas();
    User getById(Long id) throws UserNotFoundException;
    User updateUser(User user) throws UserNotFoundException;
    void deleteUser(Long id) throws UserNotFoundException;
}
