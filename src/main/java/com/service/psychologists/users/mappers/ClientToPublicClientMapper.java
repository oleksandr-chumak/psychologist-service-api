package com.service.psychologists.users.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.domain.models.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientToPublicClientMapper implements Mapper<Client, PublicClient> {

    final private ModelMapper modelMapper;

    public ClientToPublicClientMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PublicClient mapTo(Client client) {
        PublicClient publicClient = modelMapper.map(client, PublicClient.class);
        publicClient.setEmail(client.getCredentials().getEmail());
        return publicClient;
    }

    @Override
    public Client mapFrom(PublicClient publicClient) {
        Client client = this.modelMapper.map(publicClient, Client.class);
        client.setCredentials(Credentials.builder().email(publicClient.getEmail()).build());
        return client;
    }
}
