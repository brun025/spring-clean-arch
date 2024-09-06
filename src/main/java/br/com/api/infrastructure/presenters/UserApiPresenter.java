package br.com.api.infrastructure.presenters;

import br.com.api.application.user.retrieve.get.UserOutput;
import br.com.api.application.user.retrieve.list.UserListOutput;
import br.com.api.infrastructure.dtos.UserListResponse;
import br.com.api.infrastructure.dtos.UserResponse;

public interface UserApiPresenter {

	static UserResponse present(final UserOutput output) {
        return new UserResponse(
                output.id().getValue(),
                output.name(),
                output.email(),
                output.password(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
	
	static UserListResponse present(final UserListOutput output) {
        return new UserListResponse(
                output.id().getValue(),
                output.name(),
                output.email(),
                output.password(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
