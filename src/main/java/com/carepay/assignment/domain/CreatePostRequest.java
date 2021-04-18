package com.carepay.assignment.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePostRequest {
    private final String title;
    private final String content;
}
