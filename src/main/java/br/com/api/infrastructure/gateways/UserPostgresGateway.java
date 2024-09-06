package br.com.api.infrastructure.gateways;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import br.com.api.domain.pagination.Pagination;
import br.com.api.domain.pagination.SearchQuery;
import br.com.api.domain.user.User;
import br.com.api.domain.user.UserGateway;
import br.com.api.domain.user.UserID;
import br.com.api.infrastructure.persistence.UserEntity;
import br.com.api.infrastructure.persistence.UserRepository;
import br.com.api.infrastructure.utils.SpecificationUtils;

@Component
public class UserPostgresGateway implements UserGateway {

    private final UserRepository repository;
    
    public UserPostgresGateway(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User pessoa) {
    	return save(pessoa);
    }

    @Override
    public Pagination<User> findAll(final SearchQuery aQuery) {
        // Paginação
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        // Busca dinamica pelo criterio terms (name ou description)
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult =
                this.repository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(UserEntity::toAggregate).toList()
        );
    }
    
    @Override
    public Optional<User> findById(final UserID anId) {
        return this.repository.findById(anId.getValue())
                .map(UserEntity::toAggregate);
    }

    @Override
    public User update(final User aUser) {
        return save(aUser);
    }	

    @Override
    public void deleteById(final UserID anId) {
        final String anIdValue = anId.getValue();
        if (this.repository.existsById(anIdValue)) {
            this.repository.deleteById(anIdValue);
        }
    }
	
	private User save(final User aUser) {
        return this.repository.save(UserEntity.from(aUser)).toAggregate();
    }
	
	@Override
	public Boolean existsByEmail(String email) {
		return this.repository.existsByEmail(email);
	}
	
	private Specification<UserEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
