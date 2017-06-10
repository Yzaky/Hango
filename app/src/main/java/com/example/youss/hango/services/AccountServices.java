package com.example.youss.hango.services;

import android.app.ProgressDialog;

import com.example.youss.hango.infrastructure.ServiceResponse;
import com.facebook.AccessToken;


public class AccountServices {

    public AccountServices(){
    }

    public static class RegisterANewUser{

        public String userName;
        public String userEmail;
        public ProgressDialog myprogressDialog;

        public RegisterANewUser(String userName, String userEmail, ProgressDialog progressDialog) {
            this.userName = userName;
            this.userEmail = userEmail;
            this.myprogressDialog = progressDialog;
        }
    }

    public static class RegisterANewUserResponse extends ServiceResponse {

    }

    public static class RequestaUserLogIn{

        public String userEmail;
        public String userPassword;
        public ProgressDialog myprogressDialog;

        public RequestaUserLogIn(String userEmail, String userPassword, ProgressDialog progressDialog) {
            this.userEmail = userEmail;
            this.userPassword = userPassword;
            this.myprogressDialog = progressDialog;
        }

    }
    public static class RequestaUserLoginResponse extends ServiceResponse{
    }

    public static class RequestAFacebookLogin{

        public AccessToken accessToken;
        public ProgressDialog myprogressDialog;
        public String username;
        public String userEmail;

        public RequestAFacebookLogin(AccessToken accessToken, ProgressDialog progressDialog, String username, String userEmail) {
            this.accessToken = accessToken;
            this.myprogressDialog = progressDialog;
            this.username = username;
            this.userEmail = userEmail;
        }

    }
}
