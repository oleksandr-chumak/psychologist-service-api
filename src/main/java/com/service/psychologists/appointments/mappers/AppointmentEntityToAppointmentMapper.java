package com.service.psychologists.appointments.mappers;

import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.entities.ClientEntity;
import com.service.psychologists.users.domain.entities.PsychologistEntity;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEntityToAppointmentMapper implements Mapper<AppointmentEntity, Appointment> {

    final private ModelMapper modelMapper;

    final private Mapper<Client, ClientEntity> clientMapper;

    final private Mapper<Psychologist, PsychologistEntity> psychologistMapper;

    public AppointmentEntityToAppointmentMapper(
            final ModelMapper modelMapper,
            final Mapper<Client, ClientEntity> clientMapper,
            final Mapper<Psychologist, PsychologistEntity> psychologistMapper
    ) {
        this.modelMapper = modelMapper;
        this.clientMapper = clientMapper;
        this.psychologistMapper = psychologistMapper;
    }

    @Override
    public Appointment mapTo(AppointmentEntity appointmentEntity) {
        Appointment appointment = modelMapper.map(appointmentEntity, Appointment.class);
        appointment.setClient(clientMapper.mapFrom(appointmentEntity.getClient()));
        appointment.setPsychologist(psychologistMapper.mapFrom(appointmentEntity.getPsychologist()));
        return appointment;
    }

    @Override
    public AppointmentEntity mapFrom(Appointment appointment) {
        AppointmentEntity appointmentEntity = modelMapper.map(appointment, AppointmentEntity.class);
        appointmentEntity.setClient(clientMapper.mapTo(appointment.getClient()));
        appointmentEntity.setPsychologist(psychologistMapper.mapTo(appointment.getPsychologist()));
        return appointmentEntity;
    }
}
