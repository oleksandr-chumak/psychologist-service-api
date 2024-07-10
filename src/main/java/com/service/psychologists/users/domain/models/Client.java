package com.service.psychologists.users.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private Credentials credentials;

    private Date createdAt;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
