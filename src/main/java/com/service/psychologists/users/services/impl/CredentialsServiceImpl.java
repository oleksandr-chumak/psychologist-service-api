package com.service.psychologists.users.services.impl;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.CredentialsEntity;
import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.repositories.CredentialsRepository;
import com.service.psychologists.users.services.CredentialsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Optional<Credentials> findByEmailAndRole(String email, UserRole role) {
        Optional<CredentialsEntity> credentialsEntity = credentialsRepository.findByEmailAndRole(email, role);
        return credentialsEntity.map(credentialsMapper::mapFrom);
    }

    @Override
    public Optional<Credentials> findByUserDetails(UserDetails userDetails) {
        GrantedAuthority authority = userDetails.getAuthorities().iterator().next();
        String email = userDetails.getUsername();
        String parsedRole = authority.getAuthority().split("_")[1];

        if(parsedRole == null || email == null) {
            return Optional.empty();
        }

        UserRole userRole;

        try {
            userRole = UserRole.valueOf(parsedRole);
        }catch (IllegalArgumentException e){
            return Optional.empty();
        }

        return findByEmailAndRole(email, userRole);
    }
}
