package br.com.api.application.user.retrieve.list;

import java.util.Objects;

import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.pagination.SearchQuery;
import br.com.api.domain.user.UserGateway;

public class DefaultListUsersUseCase extends ListUsersUseCase {

    private final UserGateway userGateway;

    public DefaultListUsersUseCase(final UserGateway userGateway) {
        this.userGateway = Objects.requireNonNull(userGateway);
    }

    @Override
    public Pagination<UserListOutput> execute(final SearchQuery aQuery) {
        return this.userGateway.findAll(aQuery)
                .map(UserListOutput::from);
    }
}
