package com.service.psychologists.appointments.domain.models;

import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicAppointment {
    private Long id;

    private Date startTime;

    private Date endTime;

    private String comment;

    private PublicClient client;

    private PublicPsychologist psychologist;

    private Date createdAt;

    private Date updatedAt;
}
