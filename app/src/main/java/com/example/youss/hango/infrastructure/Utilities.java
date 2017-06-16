package com.example.youss.hango.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.youss.hango.activities.LoginActivity;
import com.firebase.client.Firebase;

import com.google.firebase.auth.FirebaseAuth;


public class Utilities {

    public static final String FireBaseURL="https://hango-98d15.firebaseio.com/";
    public static final String FireBaseUserReferences= FireBaseURL+ "users/";
    public static final String FireBaseHangoReferences= FireBaseURL+ "userHango/";
    public static final String MyPreferences= "MYPREFERENCES";
    public static final String Email="Email";
    public static final String Username="Username";
    public static final String EventOrderPreferences= "EVENT_ORDER_PREFERENCES";
    public static final String OrderByKey="OrderByPushKey";
    public static String encodeEmail(String userEmail)
    {
        return userEmail.replace(".",",");

    }

    public static String decodeEmail(String userEmail)
    {
        return userEmail.replace(",",".");
    }
    public static void logOut(Context context, SharedPreferences sharedPreferences, FirebaseAuth auth)
    {
        SharedPreferences.Editor Editor=sharedPreferences.edit();
        Editor.putString(Utilities.Email,null).apply();
        Editor.putString(Utilities.Username,null).apply();
        auth.signOut();
        Intent intent= new Intent(context, LoginActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
