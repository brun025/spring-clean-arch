package br.com.api.core.gateways;

import java.util.List;
import java.util.Optional;

import br.com.api.core.entities.User;

public interface UserGateway {
	
    User createPessoa(User user);
    List<User> obtainAllPessoas();
    Optional<User> getById(Long id);
    User updateUser(User user);
    void deleteUser(Long id);
}
