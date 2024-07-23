package com.service.psychologists.feedbacks.repositories.impl;

import com.service.psychologists.core.repositories.impl.EntityManagerRepositoryImpl;
import com.service.psychologists.core.repositories.interfaces.PredicateManager;
import com.service.psychologists.feedbacks.domain.entities.FeedbackEntity;
import com.service.psychologists.feedbacks.repositories.FeedbackRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackRepositoryImpl extends EntityManagerRepositoryImpl<FeedbackEntity> implements FeedbackRepository {

    public FeedbackRepositoryImpl(final EntityManager em, final PredicateManager<FeedbackEntity> predicateManager) {
        super(em, FeedbackEntity.class, predicateManager);
    }
}
