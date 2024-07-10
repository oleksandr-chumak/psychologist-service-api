package com.service.psychologists.users.domain.models;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credentials {
    private Long id;

    private String email;

    private String password;
}
