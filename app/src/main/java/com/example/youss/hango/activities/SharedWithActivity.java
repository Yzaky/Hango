package com.example.youss.hango.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.example.youss.hango.R;
import com.example.youss.hango.entities.Event;
import com.example.youss.hango.entities.SharedWithUsers;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.services.EventService;
import com.example.youss.hango.services.GetUsersService;
import com.example.youss.hango.views.EventListViews.SharedWithView.SharedWithViewHolder;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;

public class SharedWithActivity extends BaseActivity {

    private String HangoID;

    private Firebase SharedWithRef;
    private Firebase HangoRef;

    private ValueEventListener SharedWithListener;
    private ValueEventListener HangoListener;

    private com.example.youss.hango.entities.SharedWithUsers SharedWithUsers;
    private Event CurrentHango;

    private FirebaseRecyclerAdapter adapter;
    public static String HANGO_EXTRA_INFO = "HANGO_EXTRA_INFO";


    public static Intent newIntent(Context context, String HangoId) {
        Intent intent = new Intent(context, SharedWithActivity.class);
        intent.putExtra(HANGO_EXTRA_INFO, HangoId);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_hango);
        setTitle("Shared With");
        HangoID = getIntent().getStringExtra(HANGO_EXTRA_INFO);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_list_recyclerView);

        SharedWithRef = new Firebase(Utilities.FireBaseSharedWithReference + HangoID);

        HangoRef = new Firebase(Utilities.FireBaseHangoReferences + UserEmail + "/" + HangoID);

        myBus.post(new GetUsersService.GetSharedWithRequest(SharedWithRef));

        myBus.post(new EventService.GetCurrentHangoRequest(HangoRef));

        Firebase Ref = new Firebase(Utilities.FireBaseUserFriendReference + UserEmail + "/" + "usersFriends");

        adapter = new FirebaseRecyclerAdapter<User, SharedWithViewHolder>(User.class, R.layout.user_list, SharedWithViewHolder.class,
                Ref) {
            @Override
            protected void populateViewHolder(final SharedWithViewHolder sharedWithViewHolder, final User user, final int i) {

                sharedWithViewHolder.populate(user);

                if (isSharedWith(SharedWithUsers.getSharedWith(), user)) {
                    sharedWithViewHolder.userImageView.setImageResource(R.mipmap.ic_check);
                } else {
                    sharedWithViewHolder.userImageView.setImageResource(R.mipmap.ic_plus);
                }

                sharedWithViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase sharedWithRef = new Firebase(Utilities.FireBaseSharedWithReference + HangoID + "/sharedWith/" + Utilities.encodeEmail(user.getEmail()));

                        Firebase friendHangoRef = new Firebase(Utilities.FireBaseHangoReferences + Utilities.encodeEmail(user.getEmail()) + "/" + HangoID);

                        if (isSharedWith(SharedWithUsers.getSharedWith(), user)) {
                            sharedWithRef.removeValue();
                            friendHangoRef.removeValue();
                            sharedWithViewHolder.userImageView.setImageResource(R.mipmap.ic_plus);
                            updateReferences(SharedWithUsers.getSharedWith(), CurrentHango, myBus, true);
                        } else {
                            sharedWithRef.setValue(user);
                            friendHangoRef.setValue(CurrentHango);
                            sharedWithViewHolder.userImageView.setImageResource(R.mipmap.ic_check);
                            updateReferences(SharedWithUsers.getSharedWith(), CurrentHango, myBus, false);
                        }


                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public boolean isSharedWith(HashMap<String, User> usersSharedWIth, User user) {
        return usersSharedWIth != null && usersSharedWIth.size() != 0 &&
                usersSharedWIth.containsKey(Utilities.encodeEmail(user.getEmail()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
        SharedWithRef.removeEventListener(SharedWithListener);
        //HangoRef.removeEventListener(HangoListener);
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

    @Subscribe
    public void getCurrentHango(EventService.GetCurrentHangoResponse response) {
        CurrentHango = response.event;
        HangoListener = response.valueEventListener;
    }

    public static void updateReferences(HashMap<String, User> usersSharedWith, Event event, Bus myBus, boolean delete) {

        if (usersSharedWith != null && !usersSharedWith.isEmpty()) {
            for (User u : usersSharedWith.values()) {
                if (usersSharedWith.containsKey(Utilities.encodeEmail(u.getEmail()))) {
                    Firebase friendListRef = new Firebase(Utilities.FireBaseHangoReferences + Utilities.encodeEmail(u.getEmail()) + "/" + event.getid());

                    if (!delete) {
                        myBus.post(new EventService.UpdateList(friendListRef));
                    }
                }
            }
        }
        Firebase OwnerRef = new Firebase(Utilities.FireBaseHangoReferences + "/" + Utilities.encodeEmail(event.getcreatorEmail()) + "/" + event.getid());

        myBus.post(new EventService.UpdateList(OwnerRef));
    }


}

