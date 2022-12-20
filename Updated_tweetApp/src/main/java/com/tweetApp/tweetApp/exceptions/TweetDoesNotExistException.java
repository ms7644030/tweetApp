package com.tweetApp.tweetApp.exceptions;

public class TweetDoesNotExistException extends Exception {

    public TweetDoesNotExistException(String msg) {
        super(msg);
    }
}

