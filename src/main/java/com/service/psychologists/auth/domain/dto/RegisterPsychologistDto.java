package com.service.psychologists.auth.domain.dto;

import com.service.psychologists.users.domain.enums.ExperienceUnit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterPsychologistDto {

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @NotBlank(message = "Email Name is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password Name is mandatory")
    private String password;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Experience Amount is mandatory")
    private Integer experienceAmount;

    @NotBlank(message = "Experience Unit is mandatory")
    private ExperienceUnit experienceUnit;

    @NotBlank(message = "Initial Consultation is mandatory")
    private String initialConsultation;

    @NotBlank(message = "License is mandatory")
    private String license;

    @NotBlank(message = "First Name is mandatory")
    private Integer ratePerOur;

}
