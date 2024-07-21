package com.service.psychologists.appointments.services.impl;

import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.repositories.AppointmentRepository;
import com.service.psychologists.appointments.services.AppointmentService;
import com.service.psychologists.core.repositories.Order;
import com.service.psychologists.core.repositories.PredicateManager;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import com.service.psychologists.core.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    final private AppointmentRepository appointmentRepository;

    final private PredicateManager<AppointmentEntity> predicateManager;

    final private Mapper<Appointment, AppointmentEntity> mapper;

    public AppointmentServiceImpl(
            final AppointmentRepository appointmentRepository,
            final PredicateManager<AppointmentEntity> predicateManager,
            final Mapper<Appointment, AppointmentEntity> mapper
    ) {
        this.appointmentRepository = appointmentRepository;
        this.predicateManager = predicateManager;
        this.mapper = mapper;
    }

    @Override
    public Appointment create(Appointment appointment) {
        return null;
    }

    @Override
    public Appointment update(Appointment appointment) {
        return null;
    }

    @Override
    public Page<Appointment> findAllByCredentialsId(Long credentialsId) {
        return null;
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable, List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        Page<AppointmentEntity> appointmentEntities = appointmentRepository.find(searchPredicateCriteriaList, orders, pageable);
        return appointmentEntities.map(mapper::mapFrom);
    }

    @Override
    public List<Appointment> findAll(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        return List.of();
    }
}
