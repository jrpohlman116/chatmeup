package com.jrpohlman.chatmeup;

/**
 * Created by jorda on 11/29/2017.
 */

public class User {
    private String sUsername;
    private String sMessage;

    public User(String user, String message) {
        sUsername = user;
        sMessage = message;
    }

    public String getUsername() {
        return sUsername;
    }

    public String getMessage() {
        return sMessage;
    }
}
