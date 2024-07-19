package com.service.psychologists.users.domain.entities;


import com.service.psychologists.appointments.domain.entities.AppointmentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Formula(value = "concat(first_name, ' ', last_name)")
    private String fullName;

    // relations

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id")
    private CredentialsEntity credentials;

    @OneToMany(mappedBy = "client")
    private Set<AppointmentEntity> appointments;

    // timestamps

    @CreationTimestamp
    private Date createdAt;
}
