package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.mapper.PostMapper;
import com.tutran.springblog.payload.PaginationMeta;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostResponseDtoWithMeta;
import com.tutran.springblog.payload.post.PostUpdateRequest;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostResponseDto createPost(PostCreateRequest postCreateRequest) {
        String title = postCreateRequest.getTitle();
        this.validateNotDuplicatePostTitle(title);

        Post post = Post.builder()
                .title(title)
                .description(postCreateRequest.getDescription())
                .content(postCreateRequest.getContent())
                .build();

        Post newPost = postRepository.save(post);
        return postMapper.postToPostResponseDto(newPost);
    }

    @Override
    public PostResponseDtoWithMeta getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<PostResponseDto> postsData = posts.getContent().stream().map(postMapper::postToPostResponseDto).toList();

        PaginationMeta meta = new PaginationMeta();
        meta.setPageNo(pageNo);
        meta.setPageSize(pageSize);
        meta.setTotalElements(posts.getTotalElements());
        meta.setTotalPages(posts.getTotalPages());
        meta.setLast(posts.isLast());

        return PostResponseDtoWithMeta.builder().posts(postsData).meta(meta).build();
    }

    @Override
    public PostResponseDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(this.getPostNotFoundMessage(id))
        );
        return postMapper.postToPostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePostById(long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(this.getPostNotFoundMessage(id))
        );

        String title = postUpdateRequest.getTitle();
        String description = postUpdateRequest.getDescription();
        String content = postUpdateRequest.getContent();


        boolean needUpdate = false;
        if (this.isNotEmpty(title)) {
            if (!title.equals(post.getTitle())) {
                this.validateNotDuplicatePostTitle(title);
            }
            post.setTitle(title);
            needUpdate = true;
        }
        if (this.isNotEmpty(description)) {
            post.setDescription(description);
            needUpdate = true;
        }
        if (this.isNotEmpty(content)) {
            post.setContent(content);
            needUpdate = true;
        }

        if (needUpdate) {
            Post updatedPost = postRepository.save(post);
            return postMapper.postToPostResponseDto(updatedPost);
        }
        return postMapper.postToPostResponseDto(post);
    }

    @Override
    @Transactional
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(this.getPostNotFoundMessage(id))
        );

        postRepository.delete(post);
    }

    private boolean isNotEmpty(String property) {
        return property != null && !property.isEmpty();
    }

    private void validateNotDuplicatePostTitle(String title) {
        boolean isExisted = postRepository.existsByTitle(title);
        if (isExisted) {
            throw new DuplicateKeyException(this.getDuplicatePostTitleMessage(title));
        }
    }

    private String getPostNotFoundMessage(long id) {
        return String.format("Could not find post with id %s", id);
    }

    private String getDuplicatePostTitleMessage(String title) {
        return String.format("Post with title '%s' already exists", title);
    }
}
