package com.YoussefApp.Hango.live;

import com.example.youss.hango.infrastructure.Hango;


public class Module {

    public static void Register(Hango application)
    {
        new AccountServiceLive(application);

    }
}