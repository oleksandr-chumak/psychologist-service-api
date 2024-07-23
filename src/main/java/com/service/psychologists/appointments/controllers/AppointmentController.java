package com.service.psychologists.appointments.controllers;

import com.service.psychologists.appointments.domain.dto.CreateAppointmentDto;
import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.services.AppointmentService;
import com.service.psychologists.appointments.validators.AppointmentSearchParameterValidator;
import com.service.psychologists.core.annotations.ParsedOrderRequestParam;
import com.service.psychologists.core.annotations.ParsedSearchRequestParam;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.repositories.models.Order;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.services.ClientService;
import com.service.psychologists.users.services.CredentialsService;
import com.service.psychologists.users.services.PsychologistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;


@RestController
public class AppointmentController {

    final private CredentialsService credentialsService;

    final private AppointmentService appointmentService;

    final private ClientService clientService;

    final private PsychologistService psychologistService;

    public AppointmentController(
            final CredentialsService credentialsService,
            final AppointmentService appointmentService,
            final ClientService clientService,
            final PsychologistService psychologistService
    ) {
        this.credentialsService = credentialsService;
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.psychologistService = psychologistService;
    }


    @GetMapping(path = "/psychologists/{psychologistId}/appointments")
    public Page<Appointment> index(
            @PathVariable Long psychologistId,
            @ParsedSearchRequestParam(validator = AppointmentSearchParameterValidator.class) ArrayList<SearchPredicateCriteria<?>> filter,
            @ParsedOrderRequestParam(allowedValues = {"id"}) ArrayList<Order> order,
            Pageable pageable
    ) {
        System.out.println("Psychologist " + psychologistId);
        return appointmentService.findPsychologistAppointment(psychologistId, ComplexQuery
                .builder()
                .pageable(pageable)
                .filter(filter)
                .order(order)
                .build()
        );
    }

    @GetMapping(path = "/me/appointments")
    public Page<Appointment> indexMe(Authentication authentication, Pageable pageable) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return appointmentService.findAllByCredentialsId(credentials.getId(), ComplexQuery
                .builder()
                .pageable(pageable)
                .order(new ArrayList<>())
                .build()
        );
    }

    @PostMapping(path = "/psychologists/{psychologistId}/appointments")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<Appointment> createAppointment(
            @PathVariable @NotNull Long psychologistId,
            @Valid @RequestBody() CreateAppointmentDto body,
            Principal principal
    ) {

        Psychologist psychologist = psychologistService.findById(psychologistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));
        Client client = clientService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Appointment createAppointmentData = Appointment
                .builder()
                .comment(body.getComment())
                .startTime(body.getStartTime())
                .endTime(body.getEndTime())
                .psychologist(psychologist)
                .client(client)
                .build();

        Appointment createdAppointment = appointmentService.create(createAppointmentData);

        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @PostMapping(path = "/appointments/:appointmentId")
    public void show() {

    }

    @PatchMapping(path = "/appointments/:appointmentId")
    public void update() {

    }

    @DeleteMapping(path = "/appointments/:appointmentId")
    public void delete() {

    }

}