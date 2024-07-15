package com.service.psychologists.auth.services;

import com.service.psychologists.users.domain.models.Credentials;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Optional<Credentials> parseUsernameToCredentials(String username);

}
