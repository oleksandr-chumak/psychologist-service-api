package com.service.psychologists.users.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.PsychologistEntity;
import com.service.psychologists.users.domain.models.Psychologist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PsychologistToPsychologistEntityMapper implements Mapper<Psychologist, PsychologistEntity> {

    final private ModelMapper modelMapper;

    public PsychologistToPsychologistEntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PsychologistEntity mapTo(Psychologist psychologist) {
        return modelMapper.map(psychologist, PsychologistEntity.class);
    }

    @Override
    public Psychologist mapFrom(PsychologistEntity psychologistEntity) {
        return modelMapper.map(psychologistEntity, Psychologist.class);
    }
}
