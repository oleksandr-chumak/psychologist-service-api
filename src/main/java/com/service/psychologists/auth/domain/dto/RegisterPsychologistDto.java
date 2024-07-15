package com.service.psychologists.auth.domain.dto;

import com.service.psychologists.core.annotations.Enum;
import com.service.psychologists.users.domain.enums.ExperienceUnit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterPsychologistDto {

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    private String lastName;

    @NotBlank(message = "email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "description is mandatory")
    private String description;

    @NotBlank(message = "specialization is mandatory")
    private String specialization;

    @NotNull(message = "experienceAmount is mandatory")
    private Integer experienceAmount;

    @NotNull(message = "experienceUnit is mandatory")
    @Enum(enumClass = ExperienceUnit.class, message = "experienceUnit must be 'YEAR' or 'MONTH'")
    private String experienceUnit;

    @NotBlank(message = "initialConsultation is mandatory")
    private String initialConsultation;

    @NotBlank(message = "license is mandatory")
    private String license;

    @NotNull(message = "ratePerOur is mandatory")
    private int ratePerHour;

}
