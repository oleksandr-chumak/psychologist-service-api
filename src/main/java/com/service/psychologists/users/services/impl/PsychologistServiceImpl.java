package com.service.psychologists.users.services.impl;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.PsychologistEntity;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.repositories.PsychologistRepository;
import com.service.psychologists.users.services.PsychologistService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class PsychologistServiceImpl implements PsychologistService {

    final private Mapper<Psychologist, PsychologistEntity> psychologistMapper;

    final private PsychologistRepository psychologistRepository;

    public PsychologistServiceImpl(
            final PsychologistRepository psychologistRepository,
            final Mapper<Psychologist, PsychologistEntity> psychologistMapper
    ) {
        this.psychologistRepository = psychologistRepository;
        this.psychologistMapper = psychologistMapper;
    }

    @Override
    public Psychologist create(Psychologist data) {
        PsychologistEntity createdPsychologist = psychologistRepository.save(psychologistMapper.mapTo(data));
        return psychologistMapper.mapFrom(createdPsychologist);
    }

    @Override
    public Page<Psychologist> findAll(Pageable pageable) {
        Page<PsychologistEntity> psychologists = psychologistRepository.findAll(pageable);
        return psychologists.map(psychologistMapper::mapFrom);
    }

    @Override
    public Optional<Psychologist> findByEmail(String email) {
        Optional<PsychologistEntity> foundPsychologist = psychologistRepository.findByEmail(email);
        return foundPsychologist.map(psychologistMapper::mapFrom);
    }

    @Override
    public Optional<Psychologist> findById(Long id) {
        Optional<PsychologistEntity> psychologist = psychologistRepository.findById(id);
        return psychologist.map(psychologistMapper::mapFrom);
    }

}
