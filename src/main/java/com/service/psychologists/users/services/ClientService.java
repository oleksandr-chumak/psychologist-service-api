package com.service.psychologists.users.services;

import com.service.psychologists.users.domain.models.Client;

import java.util.Optional;

public interface ClientService {

    Client create(Client client);

    Optional<Client> findById(Long id);

    Optional<Client> findByEmail(String email);
}
