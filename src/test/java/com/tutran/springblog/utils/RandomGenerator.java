package com.tutran.springblog.utils;

import com.tutran.springblog.entity.Comment;
import com.tutran.springblog.entity.Post;
import com.tutran.springblog.payload.post.PostRequestDto;

import java.util.Random;
import java.util.Set;

public class RandomGenerator {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int DEFAULT_STRING_LENGTH = 10;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 1000;
    private static final Random random = new Random();

    public static PostRequestDto generateRandomPostRequestDto() {
        var postRequestDto = new PostRequestDto();
        postRequestDto.setTitle(generateRandomString());
        postRequestDto.setDescription(generateRandomString());
        postRequestDto.setContent(generateRandomString());

        return postRequestDto;
    }

    public static Post generateRandomPostWithComments() {
        var post = generateRandomPost();
        var comments = Set.of(generateRandomComment(post), generateRandomComment(post));
        post.setComments(comments);

        return post;
    }

    public static Post generateRandomPost() {
        var post = new Post();
        post.setId(generateRandomId());
        post.setTitle(generateRandomString());
        post.setDescription(generateRandomString());
        post.setContent(generateRandomString());

        return post;
    }

    public static Comment generateRandomComment(Post post) {
        var comment = new Comment();
        comment.setId(generateRandomId());
        comment.setName(generateRandomString());
        comment.setEmail(generateRandomEmail());
        comment.setBody(generateRandomString());
        comment.setPost(post);

        return comment;
    }

    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    public static String generateRandomString() {
        return generateRandomString(DEFAULT_STRING_LENGTH);
    }

    public static int generateRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int generateRandomInt() {
        return generateRandomInt(MIN_VALUE, MAX_VALUE);
    }

    public static long generateRandomId() {
        return generateRandomInt();
    }

    public static String generateRandomEmail() {
        String localPart = generateRandomString();
        String domain = generateRandomString(5);
        return localPart + "@" + domain + ".com";
    }
}
