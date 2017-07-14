package com.example.youss.hango.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.YoussefApp.Hango.live.UsersServiceLive;
import com.example.youss.hango.R;
import com.example.youss.hango.entities.SharedWithUsers;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.example.youss.hango.services.GetUsersService;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class DeleteEventDialogFragment extends BaseDialog implements View.OnClickListener {

    public static final String ExtraID = "Extra_ID";
    public static final String ExtraBOOL = "Extra_BOOL";
    private String HangoID;
    private boolean isClicked;

    private ValueEventListener valueEventListener; // interface can be used to receive events about data changes at location using queries ! it will
    // be fired when a value changes so we can know that we have changed the event name.
    private SharedWithUsers SharedWithUsers;// To store the users we share with them the event
    private Firebase SharedWithRef;

    public static DeleteEventDialogFragment newInstance(String eventId, boolean isClicked) {
        Bundle arg = new Bundle();
        arg.putString(ExtraID, eventId);
        arg.putBoolean(ExtraBOOL, isClicked);

        DeleteEventDialogFragment dialogFragment = new DeleteEventDialogFragment();
        dialogFragment.setArguments(arg);
        return dialogFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HangoID = getArguments().getString(ExtraID);
        isClicked = getArguments().getBoolean(ExtraBOOL);
        SharedWithRef = new Firebase(Utilities.FireBaseSharedWithReference + HangoID);//Creating our Firebase Reference
        mybus.post(new GetUsersService.GetSharedWithRequest(SharedWithRef));// Putting on the bus the request to get the users
        // this bus will stop at the method @Subscribe getSharedWith @ UsersServiceLive with will capture the request
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(getActivity().getLayoutInflater()
                .inflate(R.layout.delete_event, null)).setPositiveButton("Delete", null).setNegativeButton("Cancel", null)
                .setTitle("Delete A Hango ?").show();

        dialog.getButton(dialog.BUTTON_POSITIVE).setOnClickListener((View.OnClickListener) this);
        return dialog;
    }


    @Override
    public void onClick(View view) {
        if (isClicked) // delete the list
        {
            dismiss();
            delete(SharedWithUsers.getSharedWith(), mybus, HangoID, userEmail);

        } else {
            dismiss();
            getActivity().finish();
            delete(SharedWithUsers.getSharedWith(), mybus, HangoID, userEmail);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedWithRef.removeEventListener(valueEventListener);
    }

    @Subscribe
    public void getSharedWithUsers(GetUsersService.GetSharedWithResponse response) {
        valueEventListener = response.listener;
        if (response.SharedWithUsers != null) {
            SharedWithUsers = response.SharedWithUsers;

        } else {
            SharedWithUsers = new SharedWithUsers();
        }
    }

    public static void delete(HashMap<String, User> SharedWithUsers, Bus myBus, String ID, String CreatorEmail) {
        if (SharedWithUsers != null && !SharedWithUsers.isEmpty()) // Means that the event is shared with other users.

        {
            for (User u : SharedWithUsers.values())  // loop on each user detected under the event ID in the sharedWith tag

            {
                if (SharedWithUsers.containsKey(Utilities.encodeEmail(u.getEmail()))) {
                    // Get the User reference in the Users table so that we remove the event from his events list
                    Firebase FriendRef = new Firebase(Utilities.FireBaseHangoReferences + Utilities.encodeEmail(u.getEmail()) + "/" + ID);
                    //Removing process
                    Map Data = new HashMap();
                    Data.put("eventName", "Deleted");
                    FriendRef.updateChildren(Data);
                    FriendRef.removeValue();
                }
            }
        }
        //Delete the event from the creator list. This Bus will stop at the EventServiceLive deleteEvent(request).
        myBus.post(new EventService.DeleteEventRequest(CreatorEmail, ID));
    }
}
