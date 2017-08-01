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
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.views.EventListViews.SharedWithView.SharedWithViewHolder;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class SharedWithActivityNC extends BaseActivity {

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
        Intent intent = new Intent(context, SharedWithActivityNC.class);
        intent.putExtra(HANGO_EXTRA_INFO, HangoId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_hango);
        setTitle("Participated Users");
        HangoID = getIntent().getStringExtra(HANGO_EXTRA_INFO);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_list_recyclerView);

        Firebase Ref = new Firebase(Utilities.FireBaseSharedWithReference + HangoID + "/" + "sharedWith");

        adapter = new FirebaseRecyclerAdapter<User, SharedWithViewHolder>(User.class, R.layout.user_list, SharedWithViewHolder.class,
                Ref) {
            @Override
            protected void populateViewHolder(final SharedWithViewHolder sharedWithViewHolder, final User user, final int i) {
                sharedWithViewHolder.userImageView.setVisibility(View.GONE);
                sharedWithViewHolder.populate(user);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cleanup();
    }



}

