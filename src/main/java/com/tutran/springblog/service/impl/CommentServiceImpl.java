package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Comment;
import com.tutran.springblog.entity.Post;
import com.tutran.springblog.exception.CommentNotBelongingToPostException;
import com.tutran.springblog.mapper.CommentMapper;
import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import com.tutran.springblog.repository.CommentRepository;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.CommentService;
import com.tutran.springblog.utils.ErrorMessageBuilder;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto createComment(long postId, CommentCreateRequest commentCreateRequest) {
        var comment = commentMapper.commentCreateRequestToComment(commentCreateRequest);
        var post = this.getPostByIdOrThrowException(postId);
        comment.setPost(post);

        var newComment = commentRepository.save(comment);
        return commentMapper.commentToCommentResponseDto(newComment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByPostId(long postId) {
        this.getPostByIdOrThrowException(postId);

        var comments = commentRepository.findByPostId(postId);
        return comments.stream().map(commentMapper::commentToCommentResponseDto).toList();
    }

    @Override
    public CommentResponseDto getCommentById(long postId, long commentId) {
        var post = this.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        return commentMapper.commentToCommentResponseDto(comment);
    }

    private Comment getCommentByIdOrThrowException(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getCommentNotFoundErrorMessage(commentId))
        );
    }

    private Post getPostByIdOrThrowException(long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getPostNotFoundErrorMessage(postId))
        );
    }
}
