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

    final private Mapper<ClientEntity, Client> clientMapper;

    final private Mapper<PsychologistEntity, Psychologist> psychologistMapper;

    public AppointmentEntityToAppointmentMapper(
            final ModelMapper modelMapper,
            final Mapper<ClientEntity, Client> clientMapper,
            final Mapper<PsychologistEntity, Psychologist> psychologistMapper
    ) {
        this.modelMapper = modelMapper;
        this.clientMapper = clientMapper;
        this.psychologistMapper = psychologistMapper;
    }

    @Override
    public Appointment mapTo(AppointmentEntity appointmentEntity) {
        Appointment appointment = modelMapper.map(appointmentEntity, Appointment.class);
        appointment.setClient(clientMapper.mapTo(appointmentEntity.getClient()));
        appointment.setPsychologist(psychologistMapper.mapTo(appointmentEntity.getPsychologist()));
        return appointment;
    }

    @Override
    public AppointmentEntity mapFrom(Appointment appointment) {
        AppointmentEntity appointmentEntity = modelMapper.map(appointment, AppointmentEntity.class);
        appointmentEntity.setClient(clientMapper.mapFrom(appointment.getClient()));
        appointmentEntity.setPsychologist(psychologistMapper.mapFrom(appointment.getPsychologist()));
        return appointmentEntity;
    }
}
