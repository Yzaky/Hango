package com.example.youss.hango.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Bus;



public class BaseActivity extends AppCompatActivity{

    protected Hango application;
    protected Bus myBus;
    protected FirebaseAuth myAuth;
    protected FirebaseAuth.AuthStateListener myAuthStateListener;
    protected String UserEmail;
    protected String Username;
    protected SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application=(Hango)getApplication();
        myBus=application.getBus();
        myBus.register(this);
        sharedPreferences=getSharedPreferences(Utilities.MyPreferences, Context.MODE_PRIVATE);
        Username=sharedPreferences.getString(Utilities.Username,"");
        UserEmail=sharedPreferences.getString(Utilities.Email,"");
        myAuth= FirebaseAuth.getInstance();
        if(!((this instanceof LoginActivity) ||
                (this instanceof RegisterActivity)
                || (this instanceof SplashScreen)))
        {
            myAuthStateListener=new FirebaseAuth.AuthStateListener(){

                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                     FirebaseUser User=firebaseAuth.getCurrentUser();
                    if(User==null)
                    {
                        //user not logged in
                        SharedPreferences.Editor Editor=sharedPreferences.edit();
                        Editor.putString(Utilities.Email,null).apply();
                        Editor.putString(Utilities.Username,null).apply();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }

                }
            };

            if(UserEmail.equals(""))
            {
                SharedPreferences.Editor Editor=sharedPreferences.edit();
                Editor.putString(Utilities.Email,null).apply();
                Editor.putString(Utilities.Username,null).apply();
                myAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
             }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!((this instanceof LoginActivity) ||
                (this instanceof RegisterActivity)
                || (this instanceof SplashScreen)))
        {
            myAuth.addAuthStateListener(myAuthStateListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBus.unregister(this);
        if(!((this instanceof LoginActivity) ||
                (this instanceof RegisterActivity)
                || (this instanceof SplashScreen)))
        {
            myAuth.removeAuthStateListener(myAuthStateListener);
        }
    }

}
