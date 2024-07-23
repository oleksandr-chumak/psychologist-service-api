package com.service.psychologists.appointments.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAppointmentDto {
    private Date startTime;

    private Date endTime;

    private String comment;
}
