package com.carepay.assignment.service;

import com.carepay.assignment.domain.*;
import com.carepay.assignment.exceptions.BadRequestException;
import com.carepay.assignment.exceptions.NotFoundException;
import com.carepay.assignment.repository.CommentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository repo;
    private static final int MAX_COMMENT_SIZE = 100;

    /**
     *
     * @param createCommentsRequest comments to save
     * @return comment saved
     */
    @Override
    public CommentDetails createComments(CreateCommentsRequest createCommentsRequest) {
        validateRequest(createCommentsRequest);

        Comment result = repo.save(Comment.builder()
                .postId(createCommentsRequest.getPostId())
                .comment(createCommentsRequest.getComment())
                .build());

        return CommentDetails.builder()
                .id(result.getId())
                .postId(result.getPostId())
                .comment(result.getComment())
                .build();
    }

    /**
     *
     * @param pageable pageable to retrieved
     * @return retrieved comments
     */
    @Override
    public Page<CommentInfo> getComments(Pageable pageable) {
        if (pageable == null)
            throw new BadRequestException();

        final Page<Comment> pageComments = repo.findAll(pageable);

        if (pageComments.isEmpty())
            throw new BadRequestException();

        return new PageImpl<>(
                pageComments.getContent().stream()
                        .map(p -> new CommentInfo(p.getId(), p.getPostId(), p.getComment()))
                        .collect(Collectors.toList()),
                pageable, pageComments.getTotalElements());
    }

    /**
     *
     * @param id Comment id
     * @return comment
     */
    @Override
    public CommentDetails getCommentsDetails(Long id) {
        final Optional<Comment> comment = repo.findById(id);

        if (comment.isPresent())
            return CommentDetails.builder()
                    .id(comment.get().getId())
                    .postId(comment.get().getPostId())
                    .comment(comment.get().getComment())
                    .build();
        else
            throw new NotFoundException();
    }

    /**
     *
     * @param id comment id to be deleted
     */
    @Override
    public void deleteComments(Long id) {
        Optional<Comment> comment = repo.findById(id);
        if (comment.isPresent())
            repo.deleteById(id);
        else
            throw new NotFoundException();
    }

    /**
     *
     * @param createCommentsRequest request to be validated
     */
    private void validateRequest(CreateCommentsRequest createCommentsRequest) {
        if (createCommentsRequest == null || createCommentsRequest.getComment() == null ||
                (createCommentsRequest.getComment() != null
                        && createCommentsRequest.getComment().length() >= MAX_COMMENT_SIZE))
            throw new BadRequestException();

    }
}
