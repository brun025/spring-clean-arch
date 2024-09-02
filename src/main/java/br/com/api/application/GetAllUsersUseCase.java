package br.com.api.application;

import java.util.List;

import br.com.api.domain.User;

public interface GetAllUsersUseCase {

    public List<User> execute();
}
