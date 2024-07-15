package com.service.psychologists.auth.services.impl;

import com.service.psychologists.auth.services.AuthService;
import com.service.psychologists.auth.services.PasswordService;
import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientAuthService implements AuthService<Client> {

    final private PasswordService passwordService;

    final private ClientService clientService;

    public ClientAuthService(PasswordService passwordService, ClientService clientService) {
        this.passwordService = passwordService;
        this.clientService = clientService;
    }

    @Override
    public Client register(Client client) {
        String hashedPassword = passwordService.hash(client.getCredentials().getPassword());
        client.getCredentials().setPassword(hashedPassword);
        client.getCredentials().setRole(UserRole.CLIENT);

        return clientService.create(client);
    }

    @Override
    public Optional<Client> validateUserCredentials(String email, String password) {
        Optional<Client> client = clientService.findByEmail(email);

        if(client.isEmpty()) {
            return Optional.empty();
        }

        boolean isPasswordsEqual = passwordService.compare(password, client.get().getCredentials().getPassword());

        if(!isPasswordsEqual) {
            return Optional.empty();
        }

        return client;
    }

    @Override
    public boolean checkIsUserNotRegistered(String email) {
        Optional<Client> client = clientService.findByEmail(email);
        return client.isPresent();
    }
}
