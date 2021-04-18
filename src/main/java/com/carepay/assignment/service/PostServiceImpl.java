package com.carepay.assignment.service;

import javax.validation.Valid;

import com.carepay.assignment.domain.CreatePostRequest;
import com.carepay.assignment.domain.Post;
import com.carepay.assignment.domain.PostDetails;
import com.carepay.assignment.domain.PostInfo;
import com.carepay.assignment.exceptions.BadRequestException;
import com.carepay.assignment.exceptions.NotFoundException;
import com.carepay.assignment.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository repo;
    private static final int MAX_TITLE_SIZE = 100;

    /**
     * Creates a new blog post, and will return an error message in the content if any of the values is null
     * @param createPostRequest blog to be saved
     * @return PostDetails with a status message in the content
     */
    @Override
    public PostDetails createPost(@Valid CreatePostRequest createPostRequest) {
        validateRequest(createPostRequest);

        Post result = repo.save(Post.builder()
                .title(createPostRequest.getTitle())
                .content(createPostRequest.getContent())
                .build());

        return PostDetails.builder()
                .id(result.getId())
                .title(result.getTitle())
                .content(result.getContent())
                .build();
    }

    /**
     *
     * @param pageable post to retrieve
     * @return Page<PostInfo> pages from the posts retrieved
     */
    @Override
    public Page<PostInfo> getPosts(Pageable pageable) {
        if (pageable == null)
            throw new BadRequestException();

        final Page<Post> pagePosts = repo.findAll(pageable);

        if (pagePosts.isEmpty())
            throw new BadRequestException();

        return new PageImpl<>(
                pagePosts.getContent().stream()
                        .map(p -> new PostInfo(p.getId(), p.getTitle()))
                        .collect(Collectors.toList()),
                pageable, pagePosts.getTotalElements());
    }

    /**
     * Retrieve post details
     * @param id post id
     * @return Post details
     */
    @Override
    public PostDetails getPostDetails(Long id) {
        final Optional<Post> post = repo.findById(id);

        if (post.isPresent())
           return PostDetails.builder()
                   .id(post.get().getId())
                   .title(post.get().getTitle())
                   .content(post.get().getContent())
                   .build();
        else
            throw new NotFoundException();
    }

    /**
     * Delete a post
     * @param id post id
     */
    @Override
    public void deletePost(Long id) {
        Optional<Post> post = repo.findById(id);
        if (post.isPresent())
            repo.deleteById(id);
        else
            throw new NotFoundException();
    }

    /**
     *
     * @param createPostRequest request to be validated
     */
    private void validateRequest(CreatePostRequest createPostRequest) {
        if (createPostRequest == null || createPostRequest.getTitle() == null || createPostRequest.getContent() == null ||
                (createPostRequest.getTitle() != null && createPostRequest.getTitle().length() >= MAX_TITLE_SIZE))
            throw new BadRequestException();

    }
}
