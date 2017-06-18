package com.example.youss.hango.services;

import android.app.ProgressDialog;
import android.content.SharedPreferences;

import com.example.youss.hango.entities.Event;
import com.example.youss.hango.infrastructure.ServiceResponse;
import com.facebook.AccessToken;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;


public class AccountServices {

    public AccountServices(){
    }

    public static class RegisterANewUser{

        public String userName;
        public String userEmail;
        public ProgressDialog myprogressDialog;
        public SharedPreferences sharedPreferences;


        public RegisterANewUser(String userName, String userEmail, ProgressDialog myprogressDialog, SharedPreferences sharedPreferences) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.myprogressDialog = myprogressDialog;
            this.sharedPreferences = sharedPreferences;
        }

    }

    public static class RegisterANewUserResponse extends ServiceResponse {

    }

    public static class RequestaUserLogIn{

        public String userEmail;
        public String userPassword;
        public ProgressDialog myprogressDialog;
        public SharedPreferences sharedPreferences;

        public RequestaUserLogIn(String userEmail, String userPassword, ProgressDialog progressDialog, SharedPreferences sharedPreferences) {
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.myprogressDialog = progressDialog;
            this.sharedPreferences= sharedPreferences;
        }

    }
    public static class RequestaUserLoginResponse extends ServiceResponse{
    }

    public static class RequestAFacebookLogin{

        public AccessToken accessToken;
        public ProgressDialog myprogressDialog;
        public String username;
        public String userEmail;
        public SharedPreferences sharedPreferences;

        public RequestAFacebookLogin(AccessToken accessToken, ProgressDialog myprogressDialog, String username, String userEmail, SharedPreferences sharedPreferences) {
            this.accessToken = accessToken;
            this.myprogressDialog = myprogressDialog;
            this.username = username;
            this.userEmail = userEmail;
            this.sharedPreferences = sharedPreferences;
        }
    }


}
