package com.service.psychologists.users.services.impl;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.ClientEntity;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.repositories.ClientRepository;
import com.service.psychologists.users.services.ClientService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    private final Mapper<Client, ClientEntity> clientMapper;


    public ClientServiceImpl(ClientRepository clientRepository, Mapper<Client, ClientEntity> clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Client create(Client client) {
        ClientEntity clientEntity = clientMapper.mapTo(client);
        return clientMapper.mapFrom(clientRepository.save(clientEntity));
    }

    public Optional<Client> findById(Long id) {
        Optional<ClientEntity> client = clientRepository.findById(id);
        return client.map(clientMapper::mapFrom);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        Optional<ClientEntity> client = clientRepository.findByEmail(email);
        return client.map(clientMapper::mapFrom);
    }


}
