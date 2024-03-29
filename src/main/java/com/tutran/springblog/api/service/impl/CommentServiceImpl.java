package com.tutran.springblog.api.service.impl;

import com.tutran.springblog.api.entity.Comment;
import com.tutran.springblog.api.exception.CommentNotBelongingToPostException;
import com.tutran.springblog.api.mapper.CommentMapper;
import com.tutran.springblog.api.payload.comment.CommentPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.comment.CommentRequestDto;
import com.tutran.springblog.api.payload.comment.CommentResponseDto;
import com.tutran.springblog.api.payload.meta.PaginationMeta;
import com.tutran.springblog.api.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.api.repository.CommentRepository;
import com.tutran.springblog.api.service.CommentService;
import com.tutran.springblog.api.service.PostService;
import com.tutran.springblog.api.utils.ErrorMessageBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto createComment(long postId, CommentRequestDto commentRequestDto) {
        var comment = commentMapper.commentRequestDtoToComment(commentRequestDto);
        var post = postService.getPostByIdOrThrowException(postId);
        comment.setPost(post);

        var newComment = commentRepository.save(comment);
        return commentMapper.commentToCommentResponseDto(newComment);
    }

    @Override
    public ResponseDtoWithMeta<List<CommentResponseDto>> getCommentsByPostId(
            long postId,
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir
    ) {
        postService.getPostByIdOrThrowException(postId);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        List<CommentResponseDto> commentsData = comments.getContent().stream().map(commentMapper::commentToCommentResponseDto).toList();

        PaginationMeta meta = new PaginationMeta();
        meta.setPageNo(pageNo);
        meta.setPageSize(pageSize);
        meta.setTotalElements(comments.getTotalElements());
        meta.setTotalPages(comments.getTotalPages());
        meta.setLast(comments.isLast());

        return new ResponseDtoWithMeta<>(commentsData, meta);
    }

    @Override
    public CommentResponseDto getCommentById(long postId, long commentId) {
        var post = postService.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        return commentMapper.commentToCommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto patchUpdateCommentById(long postId, long commentId, CommentPartialUpdateRequestDto commentPartialUpdateRequestDto) {
        var post = postService.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        var name = commentPartialUpdateRequestDto.getName();
        var email = commentPartialUpdateRequestDto.getEmail();
        var body = commentPartialUpdateRequestDto.getBody();

        var needUpdate = false;
        if (this.isNotEmpty(name)) {
            comment.setName(name);
            needUpdate = true;
        }
        if (this.isNotEmpty(email)) {
            comment.setEmail(email);
            needUpdate = true;
        }
        if (this.isNotEmpty(body)) {
            comment.setBody(body);
            needUpdate = true;
        }

        if (needUpdate) {
            var updatedComment = commentRepository.save(comment);
            return commentMapper.commentToCommentResponseDto(updatedComment);
        }
        return commentMapper.commentToCommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateCommentById(long postId, long commentId, CommentRequestDto commentRequestDto) {
        var post = postService.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        comment.setName(commentRequestDto.getName());
        comment.setEmail(commentRequestDto.getEmail());
        comment.setBody(commentRequestDto.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.commentToCommentResponseDto(updatedComment);
    }

    @Override
    @Transactional
    public String deleteCommentById(long postId, long commentId) {
        var post = postService.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        commentRepository.delete(comment);

        return "Comment entity deleted successfully";
    }

    private boolean isNotEmpty(String property) {
        return property != null && !property.isEmpty();
    }

    private Comment getCommentByIdOrThrowException(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getCommentNotFoundErrorMessage(commentId))
        );
    }
}
