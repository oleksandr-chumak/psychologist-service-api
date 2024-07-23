package com.service.psychologists.feedbacks.services.impl;

import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.feedbacks.domain.entities.FeedbackEntity;
import com.service.psychologists.feedbacks.domain.models.Feedback;
import com.service.psychologists.feedbacks.repositories.FeedbackRepository;
import com.service.psychologists.feedbacks.services.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    private final Mapper<Feedback, FeedbackEntity> feedbackMapper;

    public FeedbackServiceImpl(
            final FeedbackRepository feedbackRepository,
            final Mapper<Feedback, FeedbackEntity> feedbackMapper
    ) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }


    @Override
    public Page<Feedback> findPsychologistFeedbacks(Long psychologistId, ComplexQuery complexQuery) {
        List<SearchPredicateCriteria<?>> criteria = complexQuery.getFilter() != null ? complexQuery.getFilter() : new ArrayList<>();
        criteria.add(new SearchPredicateCriteria<>("psychologist.id", psychologistId, EqualityOperator.EQ, ConditionOperator.AND));
        Page<FeedbackEntity> feedbackEntities = feedbackRepository.findPageable(ComplexQuery
                .builder()
                .filter(criteria)
                .order(complexQuery.getOrder())
                .join(complexQuery.getJoin())
                .pageable(complexQuery.getPageable())
                .build()
        );
        return feedbackEntities.map(feedbackMapper::mapFrom);
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        Optional<FeedbackEntity> feedbackEntity = feedbackRepository.findOne(ComplexQuery.builder()
                .filter(List.of(new SearchPredicateCriteria<>("id", id, EqualityOperator.EQ, ConditionOperator.AND)))
                .build()
        );

        return feedbackEntity.map(feedbackMapper::mapFrom);
    }

    @Override
    public Feedback create(Feedback feedback) {
        return feedbackMapper.mapFrom(feedbackRepository.create(feedbackMapper.mapTo(feedback)));
    }

    @Override
    public Feedback update(Feedback feedback) {
        return feedbackMapper.mapFrom(feedbackRepository.update(feedbackMapper.mapTo(feedback)));
    }

    @Override
    public Feedback delete(Long id) {
        return feedbackMapper.mapFrom(feedbackRepository.delete(id));
    }
}
