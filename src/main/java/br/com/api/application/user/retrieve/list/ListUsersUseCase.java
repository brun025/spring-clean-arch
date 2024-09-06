package br.com.api.application.user.retrieve.list;

import br.com.api.application.UseCase;
import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.pagination.SearchQuery;

public abstract class ListUsersUseCase
	extends UseCase<SearchQuery, Pagination<UserListOutput>> {
}
