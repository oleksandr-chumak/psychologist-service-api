package com.service.psychologists.appointments.services;

import com.service.psychologists.appointments.domain.models.Appointment;
import org.springframework.data.domain.Page;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment update(Appointment appointment);

    Page<Appointment> findAllByCredentialsId(Long credentialsId);

    Page<Appointment> findAll();
}
