package com.service.psychologists.appointments.mappers;

import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.domain.models.PublicAppointment;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import org.springframework.stereotype.Component;

@Component
public class AppointmentToPublicAppointmentMapper implements Mapper<Appointment, PublicAppointment> {


    final private Mapper<Client, PublicClient> publicClientMapper;

    final private Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper;

    public AppointmentToPublicAppointmentMapper(
            final Mapper<Client, PublicClient> publicClientMapper,
            final Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper
    ) {
        this.publicClientMapper = publicClientMapper;
        this.publicPsychologistMapper = publicPsychologistMapper;
    }

    @Override
    public PublicAppointment mapTo(Appointment appointment) {
        return PublicAppointment
                .builder()
                .id(appointment.getId())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .comment(appointment.getComment())
                .psychologist(publicPsychologistMapper.mapTo(appointment.getPsychologist()))
                .client(publicClientMapper.mapTo(appointment.getClient()))
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }

    @Override
    public Appointment mapFrom(PublicAppointment publicAppointment) {
        return Appointment
                .builder()
                .id(publicAppointment.getId())
                .startTime(publicAppointment.getStartTime())
                .endTime(publicAppointment.getEndTime())
                .comment(publicAppointment.getComment())
                .createdAt(publicAppointment.getCreatedAt())
                .updatedAt(publicAppointment.getUpdatedAt())
                .client(publicClientMapper.mapFrom(publicAppointment.getClient()))
                .psychologist(publicPsychologistMapper.mapFrom(publicAppointment.getPsychologist()))
                .build();
    }
}
