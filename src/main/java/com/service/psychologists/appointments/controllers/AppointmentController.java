package com.service.psychologists.appointments.controllers;

import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.services.AppointmentService;
import com.service.psychologists.appointments.validators.AppointmentSearchParameterValidator;
import com.service.psychologists.core.annotations.ParsedOrderRequestParam;
import com.service.psychologists.core.annotations.ParsedSearchRequestParam;
import com.service.psychologists.core.repositories.Order;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.services.CredentialsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;


@RestController
public class AppointmentController {

    final private CredentialsService credentialsService;

    final private AppointmentService appointmentService;

    public AppointmentController(final CredentialsService credentialsService, final AppointmentService appointmentService) {
        this.credentialsService = credentialsService;
        this.appointmentService = appointmentService;
    }


    @GetMapping(path = "/psychologists/{psychologistId}/appointments")
    public Page<Appointment> index(
            @PathVariable String psychologistId,
            @ParsedSearchRequestParam(validator = AppointmentSearchParameterValidator.class) ArrayList<SearchPredicateCriteria<?>> filter,
            @ParsedOrderRequestParam(allowedValues = {"id"}) ArrayList<Order> order,
            Pageable pageable
    ) {
        return appointmentService.findAll(pageable, filter, order);
    }

    @GetMapping(path = "/me/appointments")
    public void indexMe(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


    }

    @PostMapping(path = "/psychologists/:psychologistId/appointments")
    public void createAppointment() {

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