package com.example.youss.hango.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.example.youss.hango.R;
import com.example.youss.hango.services.EventService;

public class DeleteEventDialogFragment extends BaseDialog implements View.OnClickListener{

    public static final String ExtraID="Extra_ID";
    public static final String ExtraBOOL="Extra_BOOL";
    private String eventId;
    private boolean isClicked;

    public static DeleteEventDialogFragment newInstance(String eventId,boolean isClicked)
    {
        Bundle arg=new Bundle();
        arg.putString(ExtraID,eventId);
        arg.putBoolean(ExtraBOOL,isClicked);

        DeleteEventDialogFragment dialogFragment=new DeleteEventDialogFragment();
        dialogFragment.setArguments(arg);
        return dialogFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId=getArguments().getString(ExtraID);
        isClicked=getArguments().getBoolean(ExtraBOOL);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog=new AlertDialog.Builder(getActivity()).setView(getActivity().getLayoutInflater()
                .inflate(R.layout.delete_event,null)).setPositiveButton("Delete",null).setNegativeButton("Cancel",null)
                .setTitle("Delete A Hango ?").show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) this);
        return dialog;
    }


    @Override
    public void onClick(View view) {
        if(isClicked) // delete the list
        {
            dismiss();
            mybus.post(new EventService.DeleteEventRequest(userEmail,eventId)); //Will only allow the event to be deleted if he is the creator of it "Hango"
        }
        else
        {
            dismiss();
            getActivity().finish();
            mybus.post(new EventService.DeleteEventRequest(userEmail,eventId));
        }
    }
}
