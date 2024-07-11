package com.service.psychologists.auth.mappers;


import com.service.psychologists.auth.domain.dto.RegisterPsychologistDto;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Psychologist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RegisterPsychologistDtoToPsychologistMapper implements Mapper<RegisterPsychologistDto, Psychologist> {

    final private ModelMapper modelMapper;

    public RegisterPsychologistDtoToPsychologistMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Psychologist mapTo(RegisterPsychologistDto registerPsychologistDto) {
        Psychologist psychologist = modelMapper.map(registerPsychologistDto, Psychologist.class);
        psychologist.getCredentials().setEmail(registerPsychologistDto.getEmail());
        psychologist.getCredentials().setPassword(registerPsychologistDto.getPassword());
        return psychologist;
    }

    @Override
    public RegisterPsychologistDto mapFrom(Psychologist psychologist) {
        RegisterPsychologistDto registerPsychologistDto = modelMapper.map(psychologist, RegisterPsychologistDto.class);
        registerPsychologistDto.setEmail(psychologist.getCredentials().getEmail());
        registerPsychologistDto.setPassword(psychologist.getCredentials().getPassword());
        return registerPsychologistDto;
    }
}
