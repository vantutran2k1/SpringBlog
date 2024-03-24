package com.tutran.springblog.api.exception;

public class CommentNotBelongingToPostException extends RuntimeException {
    public CommentNotBelongingToPostException(String message) {
        super(message);
    }
}
