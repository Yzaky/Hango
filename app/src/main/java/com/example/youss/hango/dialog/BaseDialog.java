package com.example.youss.hango.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.example.youss.hango.infrastructure.Hango;
import com.example.youss.hango.infrastructure.Utilities;
import com.squareup.otto.Bus;

public class BaseDialog extends DialogFragment {

    protected Hango application;
    protected Bus mybus;
    protected String userEmail;
    protected String userName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application=(Hango) getActivity().getApplication();
        mybus=application.getBus();
        mybus.register(this);
        userEmail=getActivity().getSharedPreferences(Utilities.MyPreferences, Context.MODE_PRIVATE).getString(Utilities.Email,"");
        userName=getActivity().getSharedPreferences(Utilities.MyPreferences,Context.MODE_PRIVATE).getString(Utilities.Username,"");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mybus.unregister(this);
    }
}
