package br.com.api.domain;

import java.util.List;
import java.util.Optional;

public interface UserGateway {
	
    User createPessoa(User user);
    List<User> obtainAllPessoas();
    Optional<User> getById(Long id);
    User updateUser(User user);
    void deleteUser(Long id);
}
