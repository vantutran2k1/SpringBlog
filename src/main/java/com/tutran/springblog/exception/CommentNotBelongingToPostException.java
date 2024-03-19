package com.tutran.springblog.exception;

public class CommentNotBelongingToPostException extends RuntimeException {
    public CommentNotBelongingToPostException(String message) {
        super(message);
    }
}
