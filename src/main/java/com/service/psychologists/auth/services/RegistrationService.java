package com.service.psychologists.auth.services;

public interface RegistrationService<A> {

    A register(A data);

    boolean checkIsUserNotRegistered(String email);

}
