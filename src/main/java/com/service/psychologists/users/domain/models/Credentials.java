package com.service.psychologists.users.domain.models;

import com.service.psychologists.users.domain.enums.UserRole;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Credentials {
    private Long id;

    private String email;

    private String password;

    private UserRole role;

    public String getUsername() {
        return email + "_" + role;
    }
}
