package com.service.psychologists.feedbacks.domain.models;

import com.service.psychologists.users.domain.models.PublicClient;
import com.service.psychologists.users.domain.models.PublicPsychologist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicFeedback {
    private Long id;

    private String content;

    private PublicPsychologist psychologist;

    private PublicClient client;

    private Byte rating;

    private Date createdAt;

    private Date updatedAt;
}
