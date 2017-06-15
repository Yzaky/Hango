package com.example.youss.hango.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.youss.hango.R;
import com.example.youss.hango.services.EventService;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventDialogFragment extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.add_event_editText)
    EditText newHangoName;

    public static AddEventDialogFragment newInstance(){
        return new AddEventDialogFragment();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View rootView= layoutInflater.inflate(R.layout.add_event,null);
        ButterKnife.bind(this,rootView);
        AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setView(rootView).
                setPositiveButton("Create",null).setNegativeButton("Cancel",null).show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return alertDialog;
    }

    @Override
    public void onClick(View view) {
        mybus.post(new EventService.AddEventRequest(newHangoName.getText().toString(),userName,userEmail));
    }
    @Subscribe
    public void addEvent(EventService.AddEventResponse response)
    {
        if(!response.didSucceed())
        {
            newHangoName.setError(response.getError("HangoName"));
        }
        else {
            dismiss();
        }
    }
}
