package com.service.psychologists.users.services;

import com.service.psychologists.users.domain.models.Psychologist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PsychologistService {

    Page<Psychologist> findAll(Pageable pageable);

    Psychologist create(Psychologist data);

    Optional<Psychologist> findByEmail(String email);

    Optional<Psychologist> findById(Long id);
}
