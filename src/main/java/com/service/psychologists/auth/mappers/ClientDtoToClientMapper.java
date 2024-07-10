package com.service.psychologists.auth.mappers;

import com.service.psychologists.auth.domain.dto.ClientDto;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientDtoToClientMapper implements Mapper<ClientDto, Client> {

    final private ModelMapper modelMapper;

    public ClientDtoToClientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Client mapTo(ClientDto clientDto) {
        Client client = modelMapper.map(clientDto, Client.class);
        client.setCredentials(Credentials
                .builder()
                .email(clientDto.getEmail())
                .password(clientDto.getPassword())
                .build()
        );
        return client;
    }

    @Override
    public ClientDto mapFrom(Client client) {
        ClientDto clientDto = modelMapper.map(client, ClientDto.class);
        clientDto.setEmail(client.getCredentials().getEmail());
        clientDto.setPassword(client.getCredentials().getPassword());
        return clientDto;
    }
}
