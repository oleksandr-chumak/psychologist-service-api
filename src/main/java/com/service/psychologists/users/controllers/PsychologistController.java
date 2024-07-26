package com.service.psychologists.users.controllers;

import com.service.psychologists.core.models.PaginationResponse;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import com.service.psychologists.users.services.PsychologistService;
import jdk.jshell.spi.ExecutionControl;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping(path = "/psychologists")
public class PsychologistController {

    private final PsychologistService psychologistService;

    private final Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper;

    public PsychologistController(
            final PsychologistService psychologistService,
            final Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper
    ) {
        this.psychologistService = psychologistService;
        this.publicPsychologistMapper = publicPsychologistMapper;
    }

    @GetMapping(path = "/")
    public PaginationResponse<PublicPsychologist> index(Pageable pageable) {
        Page<Psychologist> psychologists = psychologistService.findAll(pageable);
        return PaginationResponse.fromPage(psychologists.map(publicPsychologistMapper::mapTo));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PublicPsychologist> show(@PathVariable @NonNull Long id) {
        Psychologist psychologist = psychologistService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));

        return new ResponseEntity<>(publicPsychologistMapper.mapTo(psychologist), HttpStatus.OK);
    }

    @GetMapping(path = "/me")
    @PreAuthorize("hasAuthority('ROLE_PSYCHOLOGIST')")
    public ResponseEntity<PublicPsychologist> showMe(Principal principal) {
        Psychologist psychologist = psychologistService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));

        return new ResponseEntity<>(publicPsychologistMapper.mapTo(psychologist), HttpStatus.OK);
    }

    @PatchMapping(path = "/me")
    public String update() throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Method not implemented");
    }


}
