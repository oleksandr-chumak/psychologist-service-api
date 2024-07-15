package com.service.psychologists.auth.controllers;

import com.service.psychologists.auth.domain.dto.LoginUserDto;
import com.service.psychologists.auth.domain.dto.RegisterPsychologistDto;
import com.service.psychologists.auth.domain.models.AuthorizationToken;
import com.service.psychologists.auth.services.AuthService;
import com.service.psychologists.auth.services.JwtService;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")
public class PsychologistAuthController {

    final private Mapper<RegisterPsychologistDto, Psychologist> registerPsychologistDtoMapper;

    final private Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper;

    final private AuthService<Psychologist> authService;

    final private JwtService jwtService;

    public PsychologistAuthController(
            final AuthService<Psychologist> authService,
            final Mapper<RegisterPsychologistDto, Psychologist> psychologistMapper,
            final Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper,
            final JwtService jwtService
    ) {
        this.authService = authService;
        this.registerPsychologistDtoMapper = psychologistMapper;
        this.publicPsychologistMapper = publicPsychologistMapper;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/register/psychologist")
    public ResponseEntity<PublicPsychologist> register(@Valid @RequestBody RegisterPsychologistDto registerPsychologistDto) {
        boolean isPsychologistAlreadyRegistered = authService.checkIsUserNotRegistered(registerPsychologistDto.getEmail());

        if (isPsychologistAlreadyRegistered) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Psychologist already exists");
        }

        Psychologist psychologist = registerPsychologistDtoMapper.mapTo(registerPsychologistDto);
        Psychologist registeredPsychologist = authService.register(psychologist);

        return new ResponseEntity<>(publicPsychologistMapper.mapTo(registeredPsychologist), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login/psychologist")
    public ResponseEntity<AuthorizationToken> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        Optional<Psychologist> psychologist = authService.validateUserCredentials(loginUserDto.getEmail(), loginUserDto.getPassword());

        if (psychologist.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return new ResponseEntity<>(new AuthorizationToken(
                jwtService.generateToken(psychologist.get().getCredentials().getUsername())),
                HttpStatus.OK
        );
    }
}
