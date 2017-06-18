package com.example.youss.hango.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.youss.hango.R;
import com.example.youss.hango.services.EventService;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangeHangoNameFragment extends BaseDialog implements View.OnClickListener{

    public static final String HANGO_EXTRA_INFO = "Hango_Extra_Info";
    private String HangoID;

    @BindView(R.id.edit_hango_EditText)
    EditText newHangoName;



    public static ChangeHangoNameFragment newInstance(ArrayList<String> HangoInfo)
    {
            Bundle args=new Bundle();
            args.putStringArrayList(HANGO_EXTRA_INFO,HangoInfo);
            ChangeHangoNameFragment dialog=new ChangeHangoNameFragment();
            dialog.setArguments(args);
            return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HangoID=getArguments().getStringArrayList(HANGO_EXTRA_INFO).get(0);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.edit_hango,null);
        ButterKnife.bind(this,view);
        newHangoName.setText(getArguments().getStringArrayList(HANGO_EXTRA_INFO).get(1));
        AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("Change",null)
                .setNegativeButton("Cancel",null).setTitle("Change Hango Title ?").show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View v) {

        mybus.post(new EventService.ChangeHangoNameRequest(newHangoName.getText().toString(),HangoID,userEmail));
    }
    @Subscribe
    public void changeHangoName(EventService.ChangeHangoNameResponse response){

        if(!response.didSucceed())
        {
            newHangoName.setError(response.getError("Hango Name"));

        }
        dismiss();
    }

}
