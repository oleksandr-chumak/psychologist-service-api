package com.service.psychologists.feedbacks.controllers;

import com.service.psychologists.core.models.PaginationResponse;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.feedbacks.domain.dto.CreateFeedbackDto;
import com.service.psychologists.feedbacks.domain.dto.UpdateFeedbackDto;
import com.service.psychologists.feedbacks.domain.models.Feedback;
import com.service.psychologists.feedbacks.domain.models.PublicFeedback;
import com.service.psychologists.feedbacks.services.FeedbackService;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.services.ClientService;
import com.service.psychologists.users.services.PsychologistService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
public class FeedbackController {

    final private FeedbackService feedbackService;

    final private PsychologistService psychologistService;

    final private ClientService clientService;

    final private Mapper<Feedback, PublicFeedback> publicFeedbackMapper;


    public FeedbackController(
            final FeedbackService feedbackService,
            final PsychologistService psychologistService,
            final ClientService clientService,
            final Mapper<Feedback, PublicFeedback> publicFeedbackMapper
    ) {
        this.feedbackService = feedbackService;
        this.psychologistService = psychologistService;
        this.clientService = clientService;
        this.publicFeedbackMapper = publicFeedbackMapper;
    }

    @GetMapping(path = "/psychologists/{psychologistsId}/feedbacks/")
    public PaginationResponse<PublicFeedback> index(@PathVariable @NotNull Long psychologistsId, Pageable pageable) {
        Psychologist psychologist = psychologistService.findById(psychologistsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));


        Page<PublicFeedback> publicFeedbackPage = feedbackService
                .findPsychologistFeedbacks(psychologist.getId(), ComplexQuery.builder().pageable(pageable).build())
                .map(publicFeedbackMapper::mapTo);

        return PaginationResponse.fromPage(publicFeedbackPage);
    }

    @GetMapping(path = "/feedbacks/{id}/")
    public ResponseEntity<Feedback> show(@PathVariable @NotNull Long id) {
        Feedback feedback = feedbackService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @PatchMapping(path = "/feedbacks/{id}")
    public ResponseEntity<Feedback> update(@PathVariable @NotNull Long id, @RequestBody UpdateFeedbackDto body) {
        Feedback feedback = feedbackService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

        return new ResponseEntity<>(feedbackService.update(Feedback.builder()
                .id(feedback.getId())
                .content(body.getContent())
                .rating(body.getRating())
                .build()),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/psychologists/{psychologistsId}/feedbacks/")
    public ResponseEntity<PublicFeedback> create(@PathVariable @NotNull Long psychologistsId, @Valid @RequestBody CreateFeedbackDto body, Principal principal) {
        Psychologist psychologist = psychologistService.findById(psychologistsId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Psychologist not found"));
        Client client = clientService.findByEmail(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Feedback feedback = feedbackService.create(Feedback.builder()
                .content(body.getContent())
                .rating(body.getRating())
                .client(client)
                .psychologist(psychologist)
                .build()
        );

        return new ResponseEntity<>(publicFeedbackMapper.mapTo(feedback), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/feedbacks/{id}")
    public ResponseEntity<Feedback> delete(@PathVariable @NotNull Long id) {
        feedbackService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Feedback not found"));

        return new ResponseEntity<>(feedbackService.delete(id), HttpStatus.OK);
    }
}


