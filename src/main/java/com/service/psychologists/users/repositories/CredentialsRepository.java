package com.service.psychologists.users.repositories;

import com.service.psychologists.users.domain.entities.CredentialsEntity;
import com.service.psychologists.users.domain.enums.UserRole;
import org.springframework.data.repository.CrudRepository;


import java.util.Optional;

public interface CredentialsRepository extends CrudRepository<CredentialsEntity, Long> {

    Optional<CredentialsEntity> findByEmailAndRole(String email, UserRole role);
}
