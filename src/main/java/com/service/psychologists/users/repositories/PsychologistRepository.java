package com.service.psychologists.users.repositories;

import com.service.psychologists.users.domain.entities.PsychologistEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PsychologistRepository extends CrudRepository<PsychologistEntity, Long> {

    @Query("from PsychologistEntity p inner join CredentialsEntity c on p.credentials.id = c.id where c.email = :email and c.role = com.service.psychologists.users.domain.enums.UserRole.PSYCHOLOGIST")
    Optional<PsychologistEntity> findByEmail(@Param("email") String email);

}
