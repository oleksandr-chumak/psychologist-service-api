package com.service.psychologists.appointments.controllers;

import com.service.psychologists.appointments.domain.dto.CreateAppointmentDto;
import com.service.psychologists.appointments.domain.dto.UpdateAppointmentDto;
import com.service.psychologists.appointments.domain.models.Appointment;
import com.service.psychologists.appointments.domain.models.PublicAppointment;
import com.service.psychologists.appointments.domain.models.PublicAppointmentWithoutClientAndPsychologist;
import com.service.psychologists.appointments.services.AppointmentService;
import com.service.psychologists.appointments.validators.AppointmentSearchParameterValidator;
import com.service.psychologists.core.annotations.ParsedOrderRequestParam;
import com.service.psychologists.core.annotations.ParsedSearchRequestParam;
import com.service.psychologists.core.models.PaginationResponse;
import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.repositories.models.Order;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import com.service.psychologists.core.utils.Mapper;
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
import java.util.List;
import java.util.Objects;


@RestController
public class AppointmentController {

    final private CredentialsService credentialsService;

    final private AppointmentService appointmentService;

    final private ClientService clientService;

    final private PsychologistService psychologistService;

    final private Mapper<Appointment, PublicAppointment> publicAppointmentMapper;

    final private Mapper<Appointment, PublicAppointmentWithoutClientAndPsychologist> publicAppointmentWithoutClientAndPsychologistMapper;

    public AppointmentController(
            final CredentialsService credentialsService,
            final AppointmentService appointmentService,
            final ClientService clientService,
            final PsychologistService psychologistService,
            final Mapper<Appointment, PublicAppointment> publicAppointmentMapper,
            final Mapper<Appointment, PublicAppointmentWithoutClientAndPsychologist> publicAppointmentWithoutClientAndPsychologistMapper
    ) {
        this.credentialsService = credentialsService;
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.psychologistService = psychologistService;
        this.publicAppointmentMapper = publicAppointmentMapper;
        this.publicAppointmentWithoutClientAndPsychologistMapper = publicAppointmentWithoutClientAndPsychologistMapper;
    }


    @GetMapping(path = "/psychologists/{psychologistId}/appointments")
    public PaginationResponse<PublicAppointmentWithoutClientAndPsychologist> index(
            @PathVariable Long psychologistId,
            @ParsedSearchRequestParam(validator = AppointmentSearchParameterValidator.class) ArrayList<SearchPredicateCriteria<?>> filter,
            @ParsedOrderRequestParam(allowedValues = {"id"}) ArrayList<Order> order,
            Pageable pageable
    ) {
        Page<PublicAppointmentWithoutClientAndPsychologist> appointments = appointmentService.findPsychologistAppointment(psychologistId, ComplexQuery
                .builder()
                .pageable(pageable)
                .filter(filter)
                .order(order)
                .build()
        ).map(publicAppointmentWithoutClientAndPsychologistMapper::mapTo);

        return PaginationResponse.fromPage(appointments);
    }

    @GetMapping(path = "/me/appointments")
    public Page<PublicAppointment> indexMe(
            Authentication authentication,
            @ParsedSearchRequestParam(validator = AppointmentSearchParameterValidator.class) ArrayList<SearchPredicateCriteria<?>> filter,
            Pageable pageable
    ) {
        // TODO: replace it with custom annotation which will return credentials @Credentials
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return appointmentService.findAllByCredentialsId(credentials.getId(), ComplexQuery
                .builder()
                .pageable(pageable)
                .filter(filter)
                .order(new ArrayList<>())
                .build()
        ).map(publicAppointmentMapper::mapTo);
    }

    @PostMapping(path = "/psychologists/{psychologistId}/appointments")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<PublicAppointment> createAppointment(
            @PathVariable @NotNull Long psychologistId,
            @Valid @RequestBody() CreateAppointmentDto body,
            Principal principal
    ) {
        Psychologist psychologist = psychologistService.findById(psychologistId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));
        Client client = clientService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        boolean isDateAvailable = appointmentService.checkIsDateAvailable(psychologistId, body.getStartTime(), body.getEndTime());

        if (!isDateAvailable) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment already exist for this period of time");
        }

        Appointment createAppointmentData = Appointment
                .builder()
                .comment(body.getComment())
                .startTime(body.getStartTime())
                .endTime(body.getEndTime())
                .psychologist(psychologist)
                .client(client)
                .build();

        Appointment createdAppointment = appointmentService.create(createAppointmentData);

        return new ResponseEntity<>(publicAppointmentMapper.mapTo(createdAppointment), HttpStatus.CREATED);
    }

    @GetMapping(path = "/appointments/{id}")
    public ResponseEntity<PublicAppointment> show(@PathVariable @NotNull Long id, Authentication authentication) {
        // TODO: replace it with custom annotation which will return credentials @Credentials
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // TODO: replace it with custom annotation to find appointment by id with join parameter
        Appointment appointment = appointmentService.findOne(ComplexQuery.builder()
                        .filter(List.of(new SearchPredicateCriteria<>("id", id, EqualityOperator.EQ, ConditionOperator.AND)))
                        .join(List.of("client", "psychologist", "client.credentials", "psychologist.credentials"))
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!(Objects.equals(appointment.getClient().getCredentials().getId(), credentials.getId()) || Objects.equals(appointment.getPsychologist().getCredentials().getId(), credentials.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment must belong to you");
        }

        return new ResponseEntity<>(publicAppointmentMapper.mapTo(appointment), HttpStatus.OK);
    }

    @PatchMapping(path = "/appointments/{id}")
    public ResponseEntity<PublicAppointment> update(@PathVariable @NotNull Long id, @RequestBody UpdateAppointmentDto body, Authentication authentication) {
        // TODO: replace it with custom annotation which will return credentials @Credentials
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // TODO: replace it with custom annotation to find appointment by id with join parameter
        Appointment appointment = appointmentService.findOne(ComplexQuery.builder()
                        .filter(List.of(new SearchPredicateCriteria<>("id", id, EqualityOperator.EQ, ConditionOperator.AND)))
                        .join(List.of("client", "psychologist", "client.credentials", "psychologist.credentials"))
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!(Objects.equals(appointment.getClient().getCredentials().getId(), credentials.getId()) || Objects.equals(appointment.getPsychologist().getCredentials().getId(), credentials.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment must belong to you");
        }

        //TODO: add check for startTime and endTime
        return new ResponseEntity<>(
                publicAppointmentMapper.mapTo(appointmentService.update(Appointment.builder()
                        .id(id)
                        .startTime(body.getStartTime())
                        .endTime(body.getEndTime())
                        .comment(body.getComment())
                        .build())
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/appointments/{id}")
    public ResponseEntity<PublicAppointment> delete(@PathVariable @NotNull Long id, Authentication authentication) {
        // TODO: replace it with custom annotation which will return credentials @Credentials
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.findByUserDetails(userDetails)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // TODO: replace it with custom annotation to find appointment by id with join parameter
        Appointment appointment = appointmentService.findOne(ComplexQuery.builder()
                        .filter(List.of(new SearchPredicateCriteria<>("id", id, EqualityOperator.EQ, ConditionOperator.AND)))
                        .join(List.of("client", "psychologist", "client.credentials", "psychologist.credentials"))
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (!(Objects.equals(appointment.getClient().getCredentials().getId(), credentials.getId()) || Objects.equals(appointment.getPsychologist().getCredentials().getId(), credentials.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Appointment must belong to you");
        }

        System.out.println("delete");

        return new ResponseEntity<>(publicAppointmentMapper.mapTo(appointmentService.delete(id)), HttpStatus.OK);
    }

}