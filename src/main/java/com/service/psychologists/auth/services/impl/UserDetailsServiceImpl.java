package com.service.psychologists.auth.services.impl;

import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.services.CredentialsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final private CredentialsService credentialsService;

    public UserDetailsServiceImpl(final CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }


    private Set<SimpleGrantedAuthority> getAuthority(Credentials credentials) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + credentials.getRole()));
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        Credentials parsedCredentials = parseUsernameToCredentials(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        Credentials foundCredentials = credentialsService.findCredentialsByEmailAndRole(parsedCredentials.getEmail(), parsedCredentials.getRole())
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new User(foundCredentials.getEmail(), foundCredentials.getPassword(), getAuthority(foundCredentials));
    }

    private Optional<Credentials> parseUsernameToCredentials(String username) {
        String[] credentials = username.split("_");

        if (credentials[0] == null || credentials[1] == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(Credentials.builder()
                .email(credentials[0])
                .role(UserRole.valueOf(credentials[1]))
                .build()
        );
    }
}
