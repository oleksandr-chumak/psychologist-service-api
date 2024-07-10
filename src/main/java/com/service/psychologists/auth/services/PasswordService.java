package com.service.psychologists.auth.services;

public interface PasswordService {

    String hash(String rawPassword);

    boolean compare(String rawPassword, String hashedPassword);

}
