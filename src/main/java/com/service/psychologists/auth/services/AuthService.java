package com.service.psychologists.auth.services;

import java.util.Optional;

public interface AuthService<A> {
    A register(A data);

    Optional<A> validateUserCredentials(String email, String password);

    boolean checkIsUserNotRegistered(String email);

}
