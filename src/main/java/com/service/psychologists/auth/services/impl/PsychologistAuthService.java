package com.service.psychologists.auth.services.impl;

import com.service.psychologists.auth.services.AuthService;
import com.service.psychologists.auth.services.PasswordService;
import com.service.psychologists.users.domain.enums.UserRole;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.services.PsychologistService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PsychologistAuthService implements AuthService<Psychologist> {

    final private PsychologistService psychologistService;

    final private PasswordService passwordService;

    public PsychologistAuthService(
            final PsychologistService psychologistService,
            final PasswordService passwordService
    ) {
        this.psychologistService = psychologistService;
        this.passwordService = passwordService;
    }


    @Override
    public Psychologist register(Psychologist psychologist) {
        String hashedPassword = passwordService.hash(psychologist.getCredentials().getPassword());
        psychologist.getCredentials().setPassword(hashedPassword);
        psychologist.getCredentials().setRole(UserRole.PSYCHOLOGIST);

        return psychologistService.create(psychologist);
    }

    @Override
    public Optional<Psychologist> login(String email, String password) {
        return Optional.empty();
    }

    @Override
    public boolean checkIsUserNotRegistered(String email) {
        Optional<Psychologist> psychologist = psychologistService.findByEmail(email);
        return psychologist.isPresent();
    }
}
