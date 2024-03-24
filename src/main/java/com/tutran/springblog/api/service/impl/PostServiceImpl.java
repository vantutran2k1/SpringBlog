package com.tutran.springblog.api.service.impl;

import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.mapper.PostMapper;
import com.tutran.springblog.api.payload.meta.PaginationMeta;
import com.tutran.springblog.api.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.api.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.api.payload.post.PostPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.post.PostRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;
import com.tutran.springblog.api.repository.PostRepository;
import com.tutran.springblog.api.service.CategoryService;
import com.tutran.springblog.api.service.PostService;
import com.tutran.springblog.api.utils.ErrorMessageBuilder;
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
    private final CategoryService categoryService;

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        String title = postRequestDto.getTitle();
        this.validateNotDuplicatePostTitle(title);

        Post post = postMapper.postRequestDtoToPost(postRequestDto);
        post.setCategory(categoryService.getCategoryByIdOrThrowException(postRequestDto.getCategoryId()));
        Post newPost = postRepository.save(post);

        return postMapper.postToPostResponseDto(newPost);
    }

    @Override
    public ResponseDtoWithMeta<List<PostResponseDto>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
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

        return new ResponseDtoWithMeta<>(postsData, meta);
    }

    @Override
    public PostDetailsResponseDto getPostById(long id) {
        var post = this.getPostByIdOrThrowException(id);
        return postMapper.postToPostDetailsResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto patchUpdatePostById(long id, PostPartialUpdateRequestDto postPartialUpdateRequestDto) {
        String title = postPartialUpdateRequestDto.getTitle();
        String description = postPartialUpdateRequestDto.getDescription();
        String content = postPartialUpdateRequestDto.getContent();

        var post = this.getPostByIdOrThrowException(id);
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
    public PostResponseDto updatePostById(long id, PostRequestDto postRequestDto) {
        var post = this.getPostByIdOrThrowException(id);

        var title = postRequestDto.getTitle();
        if (!title.equals(post.getTitle())) {
            this.validateNotDuplicatePostTitle(title);
        }

        post.setTitle(title);
        post.setDescription(postRequestDto.getDescription());
        post.setContent(postRequestDto.getContent());

        var updatedPost = postRepository.save(post);
        return postMapper.postToPostResponseDto(updatedPost);
    }

    @Override
    @Transactional
    public String deletePostById(long id) {
        var post = this.getPostByIdOrThrowException(id);
        postRepository.delete(post);

        return "Post entity deleted successfully";
    }

    @Override
    public Post getPostByIdOrThrowException(long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getPostNotFoundErrorMessage(postId))
        );
    }

    private boolean isNotEmpty(String property) {
        return property != null && !property.isEmpty();
    }

    private void validateNotDuplicatePostTitle(String title) {
        boolean isExisted = postRepository.existsByTitle(title);
        if (isExisted) {
            throw new DuplicateKeyException(ErrorMessageBuilder.getDuplicatePostTitleMessage(title));
        }
    }
}
