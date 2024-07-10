package com.service.psychologists.users.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicClient {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private String email;

    private Date createdAt;

}
