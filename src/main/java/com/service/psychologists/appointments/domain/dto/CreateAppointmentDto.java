package com.service.psychologists.appointments.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAppointmentDto {
    @NotNull(message = "startTime is required")
    private Date startTime;

    @NotNull(message = "endTime is required")
    private Date endTime;

    private String comment;
}
