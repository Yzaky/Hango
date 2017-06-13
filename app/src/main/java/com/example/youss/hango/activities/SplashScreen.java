package com.example.youss.hango.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;


public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainActivity.class));

    }
}
