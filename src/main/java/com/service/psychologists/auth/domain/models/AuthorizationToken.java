package com.service.psychologists.auth.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorizationToken {

    private String accessToken;

}
