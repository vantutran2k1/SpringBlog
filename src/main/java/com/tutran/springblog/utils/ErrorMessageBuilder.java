package com.tutran.springblog.utils;

public final class ErrorMessageBuilder {
    public static String getPostNotFoundErrorMessage(long postId) {
        return String.format("Could not find post with id '%s'", postId);
    }

    public static String getDuplicatePostTitleMessage(String title) {
        return String.format("Post with title '%s' already exists", title);
    }
}
