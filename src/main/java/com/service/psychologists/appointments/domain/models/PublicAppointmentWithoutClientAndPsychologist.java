package com.service.psychologists.appointments.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicAppointmentWithoutClientAndPsychologist {
    private Long id;

    private Date startTime;

    private Date endTime;

    private String comment;

    private Date createdAt;

    private Date updatedAt;
}
