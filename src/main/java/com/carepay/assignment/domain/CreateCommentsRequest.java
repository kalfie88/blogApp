package com.carepay.assignment.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCommentsRequest {
    private final Long postId;
    private final String comment;
}
