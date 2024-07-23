package com.service.psychologists.appointments.services.impl;

import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.repositories.AppointmentRepository;
import com.service.psychologists.appointments.services.AppointmentService;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AppointmentServiceImpl implements AppointmentService {

    final private AppointmentRepository appointmentRepository;

    final private Mapper<AppointmentEntity, Appointment> mapper;

    public AppointmentServiceImpl(
            final AppointmentRepository appointmentRepository,
            final Mapper<AppointmentEntity, Appointment> mapper
    ) {
        this.appointmentRepository = appointmentRepository;
        this.mapper = mapper;
    }

    @Override
    public Appointment create(Appointment appointment) {
        AppointmentEntity createdAppointment = appointmentRepository.create(mapper.mapFrom(appointment));
        return mapper.mapTo(createdAppointment);
    }

    @Override
    public Page<Appointment> findAllByCredentialsId(Long credentialsId, ComplexQuery complexQuery) {
        List<SearchPredicateCriteria<?>> filter = getAppointmentsByCredentialsIdCriteria(credentialsId, complexQuery);
        Page<AppointmentEntity> appointmentEntities = appointmentRepository.findPageable(ComplexQuery
                .builder()
                .order(complexQuery.getOrder())
                .filter(filter)
                .pageable(complexQuery.getPageable())
                .join(List.of(new String[]{"psychologist", "psychologist.credentials", "client", "client.credentials"}))
                .build());
        return appointmentEntities.map(mapper::mapTo);
    }

    private static List<SearchPredicateCriteria<?>> getAppointmentsByCredentialsIdCriteria(Long credentialsId, ComplexQuery complexQuery) {
        SearchPredicateCriteria<Long> clientWithCredentialsIdCriteria = new SearchPredicateCriteria<>("client.credentials.id", credentialsId, EqualityOperator.EQ, ConditionOperator.OR);
        SearchPredicateCriteria<Long> psychologistWithCredentialsIdCriteria = new SearchPredicateCriteria<>("psychologist.credentials.id", credentialsId, EqualityOperator.EQ, ConditionOperator.OR);

        List<SearchPredicateCriteria<?>> filter = complexQuery.getFilter() != null ? complexQuery.getFilter() : new ArrayList<>();
        filter.add(clientWithCredentialsIdCriteria);
        filter.add(psychologistWithCredentialsIdCriteria);
        return filter;
    }

    @Override
    public Page<Appointment> findPsychologistAppointment(Long psychologistId, ComplexQuery complexQuery) {
        SearchPredicateCriteria<Long> appointmentBelongToPsychologistCriteria = new SearchPredicateCriteria<>("psychologist.id", psychologistId, EqualityOperator.EQ, ConditionOperator.AND);

        List<SearchPredicateCriteria<?>> filter = complexQuery.getFilter() != null ? complexQuery.getFilter() : new ArrayList<>();
        filter.add(appointmentBelongToPsychologistCriteria);

        Page<AppointmentEntity> appointmentEntities = appointmentRepository.findPageable(ComplexQuery
                .builder()
                .order(complexQuery.getOrder())
                .filter(filter)
                .pageable(complexQuery.getPageable())
                .join(List.of(new String[]{"psychologist"}))
                .build());
        return appointmentEntities.map(mapper::mapTo);
    }

    @Override
    public Optional<Appointment> findOne(ComplexQuery complexQuery) {
        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findOne(ComplexQuery
                .builder()
                .filter(complexQuery.getFilter())
                .join(complexQuery.getJoin())
                .build());

        return appointmentEntity.map(mapper::mapTo);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        Optional<AppointmentEntity> appointmentEntity = appointmentRepository.findOne(ComplexQuery
                .builder()
                .filter(List.of(new SearchPredicateCriteria<>("id", id, EqualityOperator.EQ, ConditionOperator.AND)))
                .build());
        return appointmentEntity.map(mapper::mapTo);
    }

    @Override
    public Appointment delete(Long id) {
        return mapper.mapTo(appointmentRepository.delete(id));
    }

    @Override
    public Appointment update(Appointment appointment) {
        return mapper.mapTo(appointmentRepository.update(mapper.mapFrom(appointment)));
    }

    @Override
    public boolean checkIsDateAvailable(Long psychologistId, Date startTime, Date endTime) {
        List<SearchPredicateCriteria<?>> criteriaList = List.of(
                new SearchPredicateCriteria<>("startTime", startTime, EqualityOperator.GE, ConditionOperator.AND),
                new SearchPredicateCriteria<>("endTime", endTime, EqualityOperator.LE, ConditionOperator.AND),
                new SearchPredicateCriteria<>("psychologist.id", psychologistId, EqualityOperator.EQ, ConditionOperator.AND)
        );

        Optional<AppointmentEntity> appointment = appointmentRepository.findOne(ComplexQuery
                .builder()
                .filter(criteriaList)
                .join(List.of(new String[]{"psychologist"}))
                .build());

        return appointment.isEmpty();
    }
}
