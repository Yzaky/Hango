package com.example.youss.hango.infrastructure;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import android.app.Application;

import com.YoussefApp.Hango.live.Module;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.squareup.otto.Bus;


public class Hango extends Application {

    private Bus mybus;

    public Hango(){

        mybus=new Bus();
    }

    @Override
    public void onCreate(){
           super.onCreate();
            Firebase.setAndroidContext(this);
            Module.Register(this);
           FacebookSdk.sdkInitialize(getApplicationContext());
           AppEventsLogger.activateApp(this);
    }

    public Bus getBus(){
        return this.mybus;
    }



}
