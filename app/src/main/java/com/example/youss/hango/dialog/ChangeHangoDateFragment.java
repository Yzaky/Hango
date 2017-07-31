package com.example.youss.hango.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.youss.hango.R;
import com.example.youss.hango.entities.SharedWithUsers;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.example.youss.hango.services.GetUsersService;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeHangoDateFragment extends BaseDialog implements View.OnClickListener {

    public static final String HANGO_EXTRA_INFO = "Hango_Extra_Info"; // To receive the event information
    private ValueEventListener SharedWithListener; // interface can be used to receive events about data changes at location using queries ! it will
    // be fired when a value changes so we can know that we have changed the event name.
    private com.example.youss.hango.entities.SharedWithUsers SharedWithUsers; // To store the users we share with them the event
    private Firebase SharedWithRef;
    private String HangoID;

    @BindView(R.id.edit_hango_EditText)
    EditText newHangoDate;

    @BindView(R.id.calendariconedit)
    ImageView calendarIcon;

    final Calendar calendar = Calendar.getInstance();

    public static ChangeHangoDateFragment newInstance(ArrayList<String> HangoInfo) {
        Bundle args = new Bundle();
        args.putStringArrayList(HANGO_EXTRA_INFO, HangoInfo);
        ChangeHangoDateFragment dialog = new ChangeHangoDateFragment();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HangoID = getArguments().getStringArrayList(HANGO_EXTRA_INFO).get(0); // Retrieve the event ID from the Bundle
        SharedWithRef = new Firebase(Utilities.FireBaseSharedWithReference + HangoID);//Creating our Firebase Reference
        mybus.post(new GetUsersService.GetSharedWithRequest(SharedWithRef)); // Putting on the bus the request to get the users
        // this bus will stop at the method @Subscribe getSharedWith @ UsersServiceLive with will capture the request
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_hangodate, null);
        ButterKnife.bind(this, view);
        newHangoDate.setText( getArguments().getStringArrayList(HANGO_EXTRA_INFO).get(1));
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setView(view).setPositiveButton("Change", null)
                .setNegativeButton("Cancel", null).setTitle("Change Hango Date ?").show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(this);

        return alertDialog;
    }

    @Override
    public void onClick(View v) {
        //If we detect that the event is shared between users
        if (SharedWithUsers.getSharedWith() != null && !SharedWithUsers.getSharedWith().isEmpty()) {
            for (User u : SharedWithUsers.getSharedWith().values()) // loop for each user found
            {
                if (SharedWithUsers.getSharedWith().containsKey(Utilities.encodeEmail(u.getEmail()))) {
                    //NOTE THAT THE BUS HERE WILL PASS TO THE METHOD ChangeHangoName(Request) @ EvenetServiceLive that will
                    //capture that we have fired a request
                    Firebase Friendlist = new Firebase(Utilities.FireBaseHangoReferences + Utilities.encodeEmail(u.getEmail()) +
                            "/" + HangoID);
                    mybus.post(new EventService.ChangeHangoDateRequest(newHangoDate.getText().toString(), HangoID,
                            Utilities.encodeEmail(u.getEmail())));
                    //update the last time changed
                    mybus.post(new EventService.UpdateList(Friendlist));
                }
            }
        }
        mybus.post(new EventService.ChangeHangoDateRequest(newHangoDate.getText().toString(), HangoID, userEmail));
        Firebase Owner = new Firebase(Utilities.FireBaseHangoReferences + "/" + userEmail + "/" + HangoID);
        mybus.post(new EventService.UpdateList(Owner));
    }

    @Subscribe
    public void changeHangoDate(EventService.ChangeHangoDateResponse response) {

        if (!response.didSucceed()) {
            newHangoDate.setError(response.getError("Hango Date"));
        }
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedWithRef.removeEventListener(SharedWithListener);

    }

    @OnClick(R.id.calendariconedit)
    public void showDatePickerDialog(View view) {
        DatePickerDialog StartTime = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(newHangoDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        StartTime.show();
    }

    public void updateLabel(EditText Date) {
        String format = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);
        Date.setText(sdf.format(calendar.getTime()));
    }

    @Subscribe
    public void getSharedWithUsers(GetUsersService.GetSharedWithResponse response) {
        SharedWithListener = response.listener;

        if (response.SharedWithUsers != null) {
            SharedWithUsers = response.SharedWithUsers;
        } else {
            SharedWithUsers = new SharedWithUsers();
        }
    }
}
