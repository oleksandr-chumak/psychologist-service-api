package com.service.psychologists.appointments.repositories.impl;

import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import com.service.psychologists.appointments.repositories.AppointmentRepository;
import com.service.psychologists.core.repositories.PredicateManager;
import com.service.psychologists.core.repositories.impl.EntityManagerRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class AppointmentRepositoryImpl extends EntityManagerRepositoryImpl<AppointmentEntity> implements AppointmentRepository {

    public AppointmentRepositoryImpl(final EntityManager em, final PredicateManager<AppointmentEntity> predicateManager) {
        super(em, AppointmentEntity.class, predicateManager);
    }
}
