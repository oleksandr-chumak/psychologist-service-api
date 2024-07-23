package com.service.psychologists.appointments.mappers;

import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.domain.models.PublicAppointmentWithoutClientAndPsychologist;
import com.service.psychologists.core.utils.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AppointmentToPublicAppointmentWithoutClientAndPsychologistMapper implements Mapper<Appointment, PublicAppointmentWithoutClientAndPsychologist> {

    final private ModelMapper modelMapper;

    public AppointmentToPublicAppointmentWithoutClientAndPsychologistMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public PublicAppointmentWithoutClientAndPsychologist mapTo(Appointment appointment) {
        return modelMapper.map(appointment, PublicAppointmentWithoutClientAndPsychologist.class);
    }

    @Override
    public Appointment mapFrom(PublicAppointmentWithoutClientAndPsychologist publicAppointmentWithoutClientAndPsychologist) {
        return modelMapper.map(publicAppointmentWithoutClientAndPsychologist, Appointment.class);
    }
}
