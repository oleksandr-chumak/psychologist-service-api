package com.service.psychologists.users.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PsychologistToPublicPsychologistMapper implements Mapper<Psychologist, PublicPsychologist> {

    final private ModelMapper modelMapper;

    public PsychologistToPublicPsychologistMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PublicPsychologist mapTo(Psychologist psychologist) {
        PublicPsychologist publicPsychologist = modelMapper.map(psychologist, PublicPsychologist.class);
        publicPsychologist.setEmail(psychologist.getCredentials().getEmail());
        return publicPsychologist;
    }

    @Override
    public Psychologist mapFrom(PublicPsychologist publicPsychologist) {
        Psychologist psychologist = modelMapper.map(publicPsychologist, Psychologist.class);
        psychologist.getCredentials().setEmail(publicPsychologist.getEmail());
        return psychologist;
    }
}
