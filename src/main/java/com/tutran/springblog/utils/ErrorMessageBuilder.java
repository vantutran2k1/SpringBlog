package com.tutran.springblog.utils;

public final class ErrorMessageBuilder {
    private ErrorMessageBuilder() {
    }

    public static String getPostNotFoundErrorMessage(long postId) {
        return String.format("Could not find post with id '%s'", postId);
    }

    public static String getDuplicatePostTitleMessage(String title) {
        return String.format("Post with title '%s' already exists", title);
    }

    public static String getCommentNotFoundErrorMessage(long commentId) {
        return String.format("Could not find comment with id '%s'", commentId);
    }

    public static String getCommentNotBelongingToPostErrorMessage(long postId, long commentId) {
        return String.format("Comment id '%s' does not belong to post '%s'", commentId, postId);
    }

    public static String getUsernameExistedErrorMessage(String username) {
        return String.format("Username '%s' already exists", username);
    }

    public static String getEmailExistedErrorMessage(String email) {
        return String.format("Email '%s' already exists", email);
    }
}
