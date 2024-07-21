package com.service.psychologists.appointments.services;

import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.core.repositories.Order;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment update(Appointment appointment);

    Page<Appointment> findAllByCredentialsId(Long credentialsId);

    Page<Appointment> findAll(Pageable pageable, List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders);

    List<Appointment> findAll(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders);
}
