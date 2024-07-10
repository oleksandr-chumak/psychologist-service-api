package com.service.psychologists.users.repositories;

import com.service.psychologists.users.domain.entities.PsychologistEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsychologistRepository extends CrudRepository<PsychologistEntity, Long> {
}
