package com.service.psychologists.appointments.domain.models;

import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Appointment {
    private Long id;

    private Date from;

    private Date to;

    private String comment;

    private Client client;

    private Psychologist psychologist;

    private Date createdAt;

    private Date updatedAt;
}
