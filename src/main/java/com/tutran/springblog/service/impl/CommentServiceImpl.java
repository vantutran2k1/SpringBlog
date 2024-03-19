package com.tutran.springblog.service.impl;

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
        var post = postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessageBuilder.getPostNotFoundErrorMessage(postId))
        );
        comment.setPost(post);

        var newComment = commentRepository.save(comment);
        return commentMapper.commentToCommentResponseDto(newComment);
    }
}
