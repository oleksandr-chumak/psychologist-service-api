package com.service.psychologists.feedbacks.domain.models;

import com.service.psychologists.users.domain.models.Client;
import com.service.psychologists.users.domain.models.Psychologist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private Long id;

    private String content;

    private Psychologist psychologist;

    private Client client;

    private Byte rating;

    private Date createdAt;

    private Date updatedAt;
}
