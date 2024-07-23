package com.service.psychologists.appointments.services;

import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.Optional;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Page<Appointment> findAllByCredentialsId(Long credentialsId, ComplexQuery complexQuery);

    Page<Appointment> findPsychologistAppointment(Long psychologistId, ComplexQuery complexQuery);

    Optional<Appointment> findOne(ComplexQuery complexQuery);

    Optional<Appointment> findById(Long id);

    Appointment update(Appointment appointment);

    Appointment delete (Long id);

    boolean checkIsDateAvailable(Long psychologistId, Date startTime, Date endTime);
}
