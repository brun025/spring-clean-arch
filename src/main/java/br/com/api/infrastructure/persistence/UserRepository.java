package br.com.api.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	Boolean existsByEmail(String email);
	
	Page<UserEntity> findAll(Specification<UserEntity> whereClause, Pageable page);
}
