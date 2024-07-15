package com.service.psychologists.users.services;

import com.service.psychologists.users.domain.models.Psychologist;

import java.util.Optional;

public interface PsychologistService {

    Psychologist create(Psychologist data);

    Optional<Psychologist> findByEmail(String email);

    Optional<Psychologist> findById(Long id);
}
