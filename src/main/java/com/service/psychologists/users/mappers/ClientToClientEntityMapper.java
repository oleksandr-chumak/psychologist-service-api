package com.service.psychologists.users.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.ClientEntity;
import com.service.psychologists.users.domain.models.Client;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientToClientEntityMapper implements Mapper<Client, ClientEntity> {

    final private ModelMapper modelMapper;

    public ClientToClientEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ClientEntity mapTo(Client client) {
        return modelMapper.map(client, ClientEntity.class);
    }

    @Override
    public Client mapFrom(ClientEntity clientEntity) {
        return modelMapper.map(clientEntity, Client.class);
    }
}
