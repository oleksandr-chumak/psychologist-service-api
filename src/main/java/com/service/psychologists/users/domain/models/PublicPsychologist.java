package com.service.psychologists.users.domain.models;

import com.service.psychologists.users.domain.enums.ExperienceUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicPsychologist {
    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private Float rating;

    private String email;

    private Integer experienceAmount;

    private ExperienceUnit experienceUnit;

    private String license;

    private String specialization;

    private String description;

    private String initialConsultation;

    private Integer ratePerHour;

    private Date createdAt;
}
