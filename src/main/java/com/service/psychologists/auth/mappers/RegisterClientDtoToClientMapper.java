package com.service.psychologists.auth.mappers;

import com.service.psychologists.auth.domain.dto.RegisterClientDto;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterClientDtoToClientMapper implements Mapper<RegisterClientDto, Client> {

    final private ModelMapper modelMapper;

    public RegisterClientDtoToClientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Client mapTo(RegisterClientDto registerClientDto) {
        Client client = modelMapper.map(registerClientDto, Client.class);
        client.setCredentials(Credentials
                .builder()
                .email(registerClientDto.getEmail())
                .password(registerClientDto.getPassword())
                .build()
        );
        return client;
    }

    @Override
    public RegisterClientDto mapFrom(Client client) {
        RegisterClientDto registerClientDto = modelMapper.map(client, RegisterClientDto.class);
        registerClientDto.setEmail(client.getCredentials().getEmail());
        registerClientDto.setPassword(client.getCredentials().getPassword());
        return registerClientDto;
    }
}
