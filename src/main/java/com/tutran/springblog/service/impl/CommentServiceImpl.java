package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Comment;
import com.tutran.springblog.entity.Post;
import com.tutran.springblog.exception.CommentNotBelongingToPostException;
import com.tutran.springblog.mapper.CommentMapper;
import com.tutran.springblog.payload.comment.CommentRequestDto;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import com.tutran.springblog.payload.meta.PaginationMeta;
import com.tutran.springblog.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.repository.CommentRepository;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.CommentService;
import com.tutran.springblog.utils.ErrorMessageBuilder;
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
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto createComment(long postId, CommentRequestDto commentRequestDto) {
        var comment = commentMapper.commentRequestDtoToComment(commentRequestDto);
        var post = this.getPostByIdOrThrowException(postId);
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
        this.getPostByIdOrThrowException(postId);

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
        var post = this.getPostByIdOrThrowException(postId);
        var comment = this.getCommentByIdOrThrowException(commentId);
        if (comment.getPost().getId() != post.getId()) {
            throw new CommentNotBelongingToPostException(ErrorMessageBuilder.getCommentNotBelongingToPostErrorMessage(postId, commentId));
        }

        return commentMapper.commentToCommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(long postId, long commentId, CommentRequestDto commentRequestDto) {
        var post = this.getPostByIdOrThrowException(postId);
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
