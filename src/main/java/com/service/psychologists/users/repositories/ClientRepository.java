package com.service.psychologists.users.repositories;

import com.service.psychologists.users.domain.entities.ClientEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<ClientEntity, Long> {

    @Query(value = "from ClientEntity c inner join CredentialsEntity cr on c.credentials.id = cr.id where cr.email=:email")
    Optional<ClientEntity> findByCredentialsEmail(@Param("email") String email);
}
