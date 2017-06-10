package com.YoussefApp.Hango.live;

import android.accounts.Account;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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
import java.util.concurrent.TimeUnit;


public class AccountServiceLive extends LiveServiceBaseClass{

    public AccountServiceLive(Hango application) {
        super(application);
    }
    @Subscribe
    public void RegisterUser(final AccountServices.RegisterANewUser request){
        AccountServices.RegisterANewUserResponse response = new AccountServices.RegisterANewUserResponse();

        if(request.userEmail.isEmpty()) {
            response.setError("email", "Please put in your Email");
        }

        if(request.userName.isEmpty())
        {
            response.setError("username","Please put in your Name");
        }
        if(response.didSucceed())
        {
            //Toast.makeText(application.getApplicationContext(),"User is being registered",Toast.LENGTH_LONG).show();
            request.myprogressDialog.show();

            SecureRandom random = new SecureRandom();
            final String RandomPassword = new BigInteger(32,random).toString();

            auth.createUserWithEmailAndPassword(request.userEmail,RandomPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                request.myprogressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else {
                                auth.sendPasswordResetEmail(request.userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()){
                                            request.myprogressDialog.dismiss();
                                            Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                             Firebase reference=new Firebase(Utilities.FireBaseUserReferences+
                                                     Utilities.encodeEmail(request.userEmail));

                                            HashMap<String,Object> userTimeRegistered = new HashMap();
                                            userTimeRegistered.put("dateRegistered", ServerValue.TIMESTAMP);
                                            reference.child("email").setValue(request.userEmail);
                                            reference.child("name").setValue(request.userName);
                                            reference.child("didLogInUsingPassword").setValue(false);
                                            reference.child("userTimeRegistered").setValue(userTimeRegistered);

                                            Toast.makeText(application.getApplicationContext(),"Please Check Your Email",Toast.LENGTH_LONG).show();

                                            request.myprogressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }
                    });
        }

        bus.post(response);
    }

    @Subscribe
    public void logInUser(final AccountServices.RequestaUserLogIn request){
       AccountServices.RequestaUserLoginResponse response = new AccountServices.RequestaUserLoginResponse();

        if(request.userEmail.isEmpty())
        {
            response.setError("email","Please Enter Your Email");
        }
        if(request.userPassword.isEmpty())
        {
            response.setError("password","Please Enter Your Password");
        }
        if(response.didSucceed())
        {
            request.myprogressDialog.show();
            auth.signInWithEmailAndPassword(request.userEmail,request.userPassword).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                request.myprogressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                            else{
                                Firebase userLocation= new Firebase(Utilities.FireBaseUserReferences+ Utilities.encodeEmail(request.userEmail));

                                userLocation.child("didLogInUsingPassword").setValue(true);
                                request.myprogressDialog.dismiss();
                                Toast.makeText(application.getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }
            bus.post(response);
    }

    @Subscribe
    public void FacebookLogin(final AccountServices.RequestAFacebookLogin request)
    {
        request.myprogressDialog.show();

        AuthCredential authCredential= FacebookAuthProvider.getCredential(request.accessToken.getToken());

        auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    request.myprogressDialog.dismiss();
                    Toast.makeText(application.getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                else {
                    // log the user in and save it back to our database
                    final Firebase reference = new Firebase(Utilities.FireBaseUserReferences+
                            Utilities.encodeEmail(request.userEmail));
                    reference.addListenerForSingleValueEvent(new ValueEventListener() { // check if user exists or not
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null) // nothing inside our database with this reference
                            {
                                HashMap<String,Object> userTimeRegistered = new HashMap();
                                userTimeRegistered.put("dateJoined", ServerValue.TIMESTAMP);
                                reference.child("email").setValue(request.userEmail);
                                reference.child("name").setValue(request.username);
                                reference.child("didLogInUsingPassword").setValue(true);
                                reference.child("userTimeRegistered").setValue(userTimeRegistered);
                            }
                        }
                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                             request.myprogressDialog.dismiss();
                            Toast.makeText(application.getApplicationContext(),firebaseError.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                    request.myprogressDialog.dismiss();
                    Toast.makeText(application.getApplicationContext(),"Successfully Logged In",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
