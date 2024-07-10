package com.service.psychologists.auth.services.impl;

import com.service.psychologists.auth.services.PasswordService;
import com.service.psychologists.auth.services.RegistrationService;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.services.ClientService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log
public class ClientRegistrationService implements RegistrationService<Client> {

    final private PasswordService passwordService;

    final private ClientService clientService;

    public ClientRegistrationService(PasswordService passwordService, ClientService clientService) {
        this.passwordService = passwordService;
        this.clientService = clientService;
    }

    @Override
    public Client register(Client client) {
        String hashedPassword = passwordService.hash(client.getCredentials().getPassword());
        client.getCredentials().setPassword(hashedPassword);

        return clientService.create(client);
    }

    @Override
    public boolean checkIsUserNotRegistered(String email) {
        Optional<Client> client = clientService.findByEmail(email);
        return client.isPresent();
    }


}