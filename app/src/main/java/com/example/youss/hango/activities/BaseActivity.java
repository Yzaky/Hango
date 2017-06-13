package com.example.youss.hango.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.example.youss.hango.infrastructure.Hango;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.otto.Bus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BaseActivity extends AppCompatActivity{

    protected Hango application;
    protected Bus myBus;
    protected FirebaseAuth myAuth;
    protected FirebaseAuth.AuthStateListener myAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application=(Hango)getApplication();
        myBus=application.getBus();
        myBus.register(this);
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
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }
            };
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
