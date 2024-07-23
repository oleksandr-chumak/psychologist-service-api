package com.service.psychologists.feedbacks.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.feedbacks.domain.entities.FeedbackEntity;
import com.service.psychologists.feedbacks.domain.models.Feedback;
import com.service.psychologists.users.domain.entities.ClientEntity;
import com.service.psychologists.users.domain.entities.PsychologistEntity;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FeedbackToFeedbackEntityMapper implements Mapper<Feedback, FeedbackEntity> {

    final private ModelMapper modelMapper;

    final private Mapper<Client, ClientEntity> clientMapper;

    final private Mapper<Psychologist, PsychologistEntity> psychologistMapper;

    public FeedbackToFeedbackEntityMapper(
            final ModelMapper modelMapper,
            final Mapper<Client, ClientEntity> clientMapper,
            final Mapper<Psychologist, PsychologistEntity> psychologistMapper
    ) {
        this.modelMapper = modelMapper;
        this.clientMapper = clientMapper;
        this.psychologistMapper = psychologistMapper;
    }

    @Override
    public FeedbackEntity mapTo(Feedback feedback) {
        FeedbackEntity feedbackEntity = modelMapper.map(feedback, FeedbackEntity.class);
        feedbackEntity.setClient(clientMapper.mapTo(feedback.getClient()));
        feedbackEntity.setPsychologist(psychologistMapper.mapTo(feedback.getPsychologist()));
        return feedbackEntity;
    }

    @Override
    public Feedback mapFrom(FeedbackEntity feedbackEntity) {
        Feedback feedback = modelMapper.map(feedbackEntity, Feedback.class);
        feedback.setClient(clientMapper.mapFrom(feedbackEntity.getClient()));
        feedback.setPsychologist(psychologistMapper.mapFrom(feedbackEntity.getPsychologist()));
        return feedback;
    }
}
