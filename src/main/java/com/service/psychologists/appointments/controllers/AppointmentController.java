package com.service.psychologists.appointments.controllers;

import com.service.psychologists.appointments.validators.AppointmentSearchParameterValidator;
import com.service.psychologists.core.annotations.ParsedSearchRequestParam;
import com.service.psychologists.users.domain.models.Credentials;
import com.service.psychologists.users.services.CredentialsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;


@RestController
public class AppointmentController {

    final private CredentialsService credentialsService;

    public AppointmentController(final CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }


    @GetMapping(path = "/psychologists/{psychologistId}/appointments")
    public String index(
            @PathVariable String psychologistId,
            @ParsedSearchRequestParam(
                    name = "filter",
                    validator = AppointmentSearchParameterValidator.class
            ) ArrayList<?> filter
    ) {
        System.out.println("filter");
        System.out.println(filter);
        return "working";
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