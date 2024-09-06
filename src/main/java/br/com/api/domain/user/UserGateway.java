package br.com.api.domain.user;

import java.util.Optional;

import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.pagination.SearchQuery;

public interface UserGateway {
	
    User create(User user);
    Pagination<User> findAll(SearchQuery aQuery);
    Optional<User> findById(UserID id);
    Boolean existsByEmail(String email);
    User update(User user);
    void deleteById(UserID id);
}
