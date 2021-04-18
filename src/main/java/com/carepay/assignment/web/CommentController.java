package com.carepay.assignment.web;

import com.carepay.assignment.domain.*;
import com.carepay.assignment.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/posts/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CommentController {

    private final CommentsService commentsService;

    @GetMapping
    public Page<CommentInfo> getComments(Pageable pageable) {
        return commentsService.getComments(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDetails createComments(@Valid @RequestBody CreateCommentsRequest createCommentsRequest) {
        return commentsService.createComments(createCommentsRequest);
    }

    @GetMapping("{id}")
    public CommentDetails getCommentsDetails(@PathVariable("id") final Long id) {
        return commentsService.getCommentsDetails(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable("id") final Long id) {
        commentsService.deleteComments(id);
    }
}
