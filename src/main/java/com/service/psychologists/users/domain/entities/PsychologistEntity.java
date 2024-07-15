package com.service.psychologists.users.domain.entities;

import com.service.psychologists.users.domain.enums.ExperienceUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "psychologists")
public class PsychologistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Formula(value = "concat(first_name, ' ', last_name)")
    private String fullName;

    @Column(nullable = false)
    private Integer experienceAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExperienceUnit experienceUnit;

    @Column(nullable = false)
    private String license;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String initialConsultation;

    @Column(nullable = false)
    private Integer ratePerHour;

    // Relations

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id")
    private CredentialsEntity credentials;


    // timestamps

    @CreationTimestamp
    private Date createdAt;
}
