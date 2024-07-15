package com.service.psychologists.auth.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.CredentialsEntity;
import com.service.psychologists.users.domain.models.Credentials;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CredentialsToCredentialsEntityMapper implements Mapper<Credentials, CredentialsEntity> {

    final private ModelMapper modelMapper;

    public CredentialsToCredentialsEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CredentialsEntity mapTo(Credentials credentials) {
        return modelMapper.map(credentials, CredentialsEntity.class);
    }

    @Override
    public Credentials mapFrom(CredentialsEntity credentialsEntity) {
        return modelMapper.map(credentialsEntity, Credentials.class);
    }
}
