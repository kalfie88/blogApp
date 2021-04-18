package com.carepay.assignment.service;

import com.carepay.assignment.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface CommentsService {
    CommentDetails createComments(@Valid CreateCommentsRequest createCommentsRequest);

    Page<CommentInfo> getComments(final Pageable pageable);

    CommentDetails getCommentsDetails(Long id);

    void deleteComments(Long id);
}
