package com.service.psychologists.appointments.repositories;

import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository extends CrudRepository<AppointmentEntity, Long>, JpaSpecificationExecutor<AppointmentEntity> {
}
