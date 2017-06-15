package com.YoussefApp.Hango.live;

import com.example.youss.hango.infrastructure.Hango;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.otto.Bus;


public class LiveServiceBaseClass {

    protected Bus myBus;
    protected Hango application;
    protected FirebaseAuth auth;

    public LiveServiceBaseClass(Hango application) {
        this.application = application;
        myBus =application.getBus();
        myBus.register(this);
        auth =  FirebaseAuth.getInstance();
    }
}
