package com.service.psychologists.users.services;

import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Credentials;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface CredentialsService {

    Optional<Credentials> findById(Long id);

    Optional<Credentials> findByEmailAndRole(String email, UserRole role);

    Optional<Credentials> findByUserDetails(UserDetails userDetails);

}
