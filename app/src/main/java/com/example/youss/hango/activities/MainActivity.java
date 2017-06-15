package com.example.youss.hango.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.youss.hango.R;
import com.example.youss.hango.dialog.AddEventDialogFragment;
import com.example.youss.hango.entities.Event;
import com.example.youss.hango.entities.User;
import com.example.youss.hango.infrastructure.Utilities;
import com.example.youss.hango.views.EventListViews.EventsListViewHolder;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_EVAB)
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter myAdapter;

    private String bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        recyclerView=(RecyclerView)findViewById(R.id.activity_main_myRecyclerView);

        if(Username.contains(" "))
        {
            bar="Welcome "+Username.substring(0,Username.indexOf(" "));
        }
        else {
            bar="Welcome "+Username;
        }
        getSupportActionBar().setTitle(bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firebase EventRef = new Firebase(Utilities.FireBaseHangoReferences + UserEmail);
        myAdapter = new FirebaseRecyclerAdapter<Event, EventsListViewHolder>(Event.class, R.layout.events_list, EventsListViewHolder.class, EventRef) {
            @Override
            protected void populateViewHolder(EventsListViewHolder eventsListViewHolder, final Event event, int i) {
                eventsListViewHolder.populate(event);
                eventsListViewHolder.LayoutView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),event.geteventName()
                                +" was clicked",Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                ProgressDialog myProgressDialog = new ProgressDialog(this);
                myProgressDialog.setTitle("Loading...");
                myProgressDialog.setMessage("Logging Out");
                myProgressDialog.setCancelable(false);
                SharedPreferences.Editor Editor=sharedPreferences.edit();
                Editor.putString(Utilities.Email,null).apply();
                Editor.putString(Utilities.Username,null).apply();
                myAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                myProgressDialog.dismiss();
        }

        return true;
    }

    @OnClick(R.id.activity_main_EVAB)
    public void setFloatingActionButton(){
        DialogFragment dialogFragment= AddEventDialogFragment.newInstance();
        dialogFragment.show(getFragmentManager(),AddEventDialogFragment.class.getSimpleName());
    }
}
