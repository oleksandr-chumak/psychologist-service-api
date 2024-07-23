package com.service.psychologists.feedbacks.services;

import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.feedbacks.domain.models.Feedback;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface FeedbackService {

    Page<Feedback> findPsychologistFeedbacks(Long psychologistId, ComplexQuery complexQuery);

    Optional<Feedback> findById(Long id);

    Feedback create(Feedback feedback);

    Feedback update(Feedback feedback);

    Feedback delete(Long id);
}
