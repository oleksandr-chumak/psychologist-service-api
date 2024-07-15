package com.service.psychologists.users.services;

import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Credentials;

import java.util.Optional;

public interface CredentialsService {

    Optional<Credentials> findById(Long id);

    Optional<Credentials> findCredentialsByEmailAndRole(String email, UserRole role);

}
