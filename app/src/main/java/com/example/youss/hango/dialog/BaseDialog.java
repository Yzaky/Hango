package com.example.youss.hango.dialog;

import android.app.DialogFragment;
import android.os.Bundle;

import com.example.youss.hango.infrastructure.Hango;
import com.squareup.otto.Bus;

public class BaseDialog extends DialogFragment {

    protected Hango application;
    protected Bus mybus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application=(Hango) getActivity().getApplication();
        mybus=application.getBus();
        mybus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mybus.unregister(this);
    }
}
