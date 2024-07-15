package com.service.psychologists.users.services.impl;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.CredentialsEntity;
import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.repositories.CredentialsRepository;
import com.service.psychologists.users.services.CredentialsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    final private Mapper<Credentials, CredentialsEntity> credentialsMapper;

    final private CredentialsRepository credentialsRepository;

    public CredentialsServiceImpl(
            final CredentialsRepository credentialsRepository,
            final Mapper<Credentials, CredentialsEntity> credentialsMapper
    ) {
        this.credentialsRepository = credentialsRepository;
        this.credentialsMapper = credentialsMapper;
    }

    @Override
    public Optional<Credentials> findById(Long id) {
        Optional<CredentialsEntity> credentialsEntity = credentialsRepository.findById(id);
        return credentialsEntity.map(credentialsMapper::mapFrom);
    }

    @Override
    public Optional<Credentials> findCredentialsByEmailAndRole(String email, UserRole role) {
        Optional<CredentialsEntity> credentialsEntity = credentialsRepository.findByEmailAndRole(email, role);
        return credentialsEntity.map(credentialsMapper::mapFrom);
    }
}
