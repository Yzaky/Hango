package com.example.youss.hango.infrastructure;

import com.firebase.client.Firebase;


public class Utilities {

    public static final String FireBaseURL="https://hango-98d15.firebaseio.com/";
    public static final String FireBaseUserReferences= FireBaseURL+ " users/";

    public static String encodeEmail(String userEmail)
    {
        return userEmail.replace(".",",");

    }
}
