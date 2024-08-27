package br.com.api.core.usecases;

import java.util.List;

import br.com.api.core.entities.User;

public interface GetAllUsersUseCase {

    public List<User> execute();
}
