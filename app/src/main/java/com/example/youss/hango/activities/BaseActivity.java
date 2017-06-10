package com.example.youss.hango.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.example.youss.hango.infrastructure.Hango;
import com.squareup.otto.Bus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BaseActivity extends AppCompatActivity{

    protected Hango application;
    protected Bus myBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application=(Hango)getApplication();
        myBus=application.getBus();
        myBus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myBus.unregister(this);
    }

}
