package com.service.psychologists.feedbacks.mappers;

import com.service.psychologists.core.utils.Mapper;
import com.service.psychologists.feedbacks.domain.models.Feedback;
import com.service.psychologists.feedbacks.domain.models.PublicFeedback;
import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import org.springframework.stereotype.Component;

@Component
public class FeedbackToPublicFeedbackMapper implements Mapper<Feedback, PublicFeedback> {

    final private Mapper<Client, PublicClient> publicClientMapper;

    final private Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper;

    public FeedbackToPublicFeedbackMapper(
            final Mapper<Client, PublicClient> publicClientMapper,
            final Mapper<Psychologist, PublicPsychologist> publicPsychologistMapper
    ) {
        this.publicClientMapper = publicClientMapper;
        this.publicPsychologistMapper = publicPsychologistMapper;
    }


    @Override
    public PublicFeedback mapTo(Feedback feedback) {
        return PublicFeedback.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .content(feedback.getContent())
                .client(publicClientMapper.mapTo(feedback.getClient()))
                .psychologist(publicPsychologistMapper.mapTo(feedback.getPsychologist()))
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getUpdatedAt())
                .build();
    }

    @Override
    public Feedback mapFrom(PublicFeedback publicFeedback) {
        return Feedback.builder()
                .id(publicFeedback.getId())
                .rating(publicFeedback.getRating())
                .content(publicFeedback.getContent())
                .client(publicClientMapper.mapFrom(publicFeedback.getClient()))
                .psychologist(publicPsychologistMapper.mapFrom(publicFeedback.getPsychologist()))
                .createdAt(publicFeedback.getCreatedAt())
                .updatedAt(publicFeedback.getUpdatedAt())
                .build();
    }
}
