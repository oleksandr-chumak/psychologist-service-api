package com.service.psychologists.feedbacks.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateFeedbackDto {

    @NotEmpty(message = "Content is required")
    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Rating is required")
    private Byte rating;

}
