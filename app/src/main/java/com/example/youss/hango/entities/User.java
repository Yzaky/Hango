package com.example.youss.hango.entities;

import java.util.HashMap;


public class User {
    private String Useremail;
    private String Username;
    private HashMap<String,Object> UserdateJoined;
    private boolean hasLoggedWithPassword;

    public User(String email, String name, HashMap<String, Object> dateJoined, boolean hasLoggedWithPassword) {
        this.Useremail = email;
        this.Username = name;
        this.UserdateJoined = dateJoined;
        this.hasLoggedWithPassword = hasLoggedWithPassword;
    }

    public String getEmail() {
        return Useremail;
    }

    public String getName() {
        return Username;
    }

    public HashMap<String, Object> usergetDateJoined() {
        return UserdateJoined;
    }

    public boolean didUserLogInWithPassword() {
        return hasLoggedWithPassword;
    }
}
