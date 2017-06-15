package com.YoussefApp.Hango.live;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.youss.hango.activities.LoginActivity;
import com.example.youss.hango.activities.MainActivity;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.AccountServices;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.squareup.otto.Subscribe;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

public class AccountServicesLive extends LiveServiceBaseClass {
    public AccountServicesLive(Hango application) {
        super(application);
    }

    @Subscribe
    public void RegisterUser(final AccountServices.RegisterANewUser request){
        AccountServices.RegisterANewUserResponse response = new AccountServices.RegisterANewUserResponse();

        if (request.userEmail.isEmpty()){
            response.setError("email","Email Can Not Be Empty.");
        }

        if (request.userName.isEmpty()){
            response.setError("userName","Name Can Not Be Empty.");
        }

        if (response.didSucceed()){
            request.myprogressDialog.show();

            SecureRandom random = new SecureRandom();
            final String randomPassword = new BigInteger(32,random).toString();



            auth.createUserWithEmailAndPassword(request.userEmail,randomPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                request.myprogressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            } else{
                                auth.sendPasswordResetEmail(request.userEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (!task.isSuccessful()){
                                                    request.myprogressDialog.dismiss();
                                                    Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                } else{
                                                    Firebase reference = new Firebase(Utilities.FireBaseUserReferences + Utilities.encodeEmail(request.userEmail));

                                                    HashMap<String,Object> timeJoined = new HashMap<>();
                                                    timeJoined.put("dateJoined", ServerValue.TIMESTAMP);
                                                    reference.child("email").setValue(request.userEmail);
                                                    reference.child("name").setValue(request.userName);
                                                    reference.child("hasLoggedInWithPassword").setValue(false);
                                                    reference.child("timeJoined").setValue(timeJoined);

                                                    Toast.makeText(application.getApplicationContext(),"Please Check Your Email",Toast.LENGTH_LONG)
                                                            .show();

                                                    request.myprogressDialog.dismiss();
                                                    Intent intent = new Intent(application.getApplicationContext(), LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    application.startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }

                    });

        }

        myBus.post(response);
    }

    @Subscribe
    public void LogInUser(final AccountServices.RequestaUserLogIn request){
        AccountServices.RequestaUserLoginResponse response = new AccountServices.RequestaUserLoginResponse();

        if (request.userEmail.isEmpty()){
            response.setError("email","Email Can Not Be Empty.");
        }

        if (request.userPassword.isEmpty()){
            response.setError("password","Please enter your password");
        }

        if (response.didSucceed()){
            request.myprogressDialog.show();
            auth.signInWithEmailAndPassword(request.userEmail,request.userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                request.myprogressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            } else{
                                final Firebase userLocation = new Firebase(Utilities.FireBaseUserReferences + Utilities.encodeEmail(request.userEmail));
                                userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (user!=null){
                                            userLocation.child("hasLoggedInWithPassword").setValue(true);
                                            SharedPreferences sharedPreferences = request.sharedPreferences;
                                            sharedPreferences.edit().putString(Utilities.Email,Utilities.encodeEmail(user.getEmail())).apply();
                                            sharedPreferences.edit().putString(Utilities.Username,user.getName()).apply();

                                            request.myprogressDialog.dismiss();
                                            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                                            application.startActivity(intent);


                                        } else {
                                            request.myprogressDialog.dismiss();
                                            Toast.makeText(application.getApplicationContext(),"An error Occured",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        request.myprogressDialog.dismiss();
                                        Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });

        }
        myBus.post(response);
    }


    @Subscribe
    public void FacebookLogin(final AccountServices.RequestAFacebookLogin request){
        request.myprogressDialog.show();

        AuthCredential authCredential = FacebookAuthProvider.getCredential(request.accessToken.getToken());


        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            request.myprogressDialog.dismiss();
                            Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        } else{
                            final Firebase reference = new Firebase(Utilities.FireBaseUserReferences + Utilities.encodeEmail(request.userEmail));
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null){
                                        HashMap<String,Object> timeJoined = new HashMap<>();
                                        timeJoined.put("dateJoined", ServerValue.TIMESTAMP);

                                        reference.child("email").setValue(request.userEmail);
                                        reference.child("name").setValue(request.username);
                                        reference.child("hasLoggedInWithPassword").setValue(true);
                                        reference.child("timeJoined").setValue(timeJoined);

                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    request.myprogressDialog.dismiss();
                                    Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
                            SharedPreferences sharedPreferences = request.sharedPreferences;
                            sharedPreferences.edit().putString(Utilities.Email,Utilities.encodeEmail(request.userEmail)).apply();
                            sharedPreferences.edit().putString(Utilities.Username,request.username).apply();

                            request.myprogressDialog.dismiss();
                            Intent intent = new Intent(application.getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK);
                            application.startActivity(intent);

                        }
                    }
                });
    }


}